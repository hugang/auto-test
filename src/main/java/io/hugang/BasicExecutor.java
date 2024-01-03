package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Commands;
import io.hugang.bean.ICommand;
import io.hugang.config.AutoTestConfig;
import io.hugang.execute.impl.RecorderCommand;
import io.hugang.util.CommandParserUtil;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * main executor class
 *
 * @author hugang
 */
public class BasicExecutor {
    private static final Log log = LogFactory.get();


    public BasicExecutor() {
    }

    /**
     * initialize web driver
     */
    public void init(AutoTestConfig autoTestConfig) {
        // create a web driver by webDriver
        Configuration.browser = autoTestConfig.getWebDriverPath();
        // set the download path
        Configuration.reportsFolder = autoTestConfig.getFileDownloadPath();
        // store the browser options
        Map<String, Object> optionsMap = new HashMap<>();
        // set pdf file always open externally
        optionsMap.put("plugins.always_open_pdf_externally", true);
        // set the download path
        optionsMap.put("download.default_directory", autoTestConfig.getFileDownloadPath());
        // set browser size
        Dimension targetWindowSize = new Dimension(autoTestConfig.getWidth(), autoTestConfig.getHeight());

        WebDriver driver;
        // create chrome webdriver
        switch (autoTestConfig.getWebDriverName()) {
            case "chrome": {
                System.setProperty("webdriver.chrome.driver", autoTestConfig.getWebDriverPath());
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
                // binary path
                if (ObjectUtil.isNotEmpty(autoTestConfig.getBrowserBinaryPath())) {
                    options.setBinary(autoTestConfig.getBrowserBinaryPath());
                }
                options.addArguments("--remote-allow-origins=*");
                options.setExperimentalOption("prefs", optionsMap);
                driver = new ChromeDriver(options);
                break;
            }
            // create edge webdriver
            case "edge": {
                System.setProperty("webdriver.edge.driver", autoTestConfig.getWebDriverPath());
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
                // binary path
                if (ObjectUtil.isNotEmpty(autoTestConfig.getBrowserBinaryPath())) {
                    options.setBinary(autoTestConfig.getBrowserBinaryPath());
                }
                options.addArguments("--remote-allow-origins=*");
                options.setExperimentalOption("prefs", optionsMap);
                driver = new EdgeDriver(options);
                break;
            }
            // create firefox webdriver
            case "firefox": {
                System.setProperty("webdriver.gecko.driver", autoTestConfig.getWebDriverPath());
                FirefoxOptions options = new FirefoxOptions();
                // binary path
                if (ObjectUtil.isNotEmpty(autoTestConfig.getBrowserBinaryPath())) {
                    options.setBinary(autoTestConfig.getBrowserBinaryPath());
                }
                driver = new FirefoxDriver(options);
                break;
            }
            default:
                throw new RuntimeException("web driver not supported");
        }
        // set the browser size
        driver.manage().window().setSize(targetWindowSize);
        // set the driver
        WebDriverRunner.setWebDriver(driver);
    }

    /**
     * method to execute the commands
     */
    private void execute(AutoTestConfig autoTestConfig, Dict variablesMap) {
        if (autoTestConfig.isCronEnabled()) {
            ReentrantLock lock = new ReentrantLock();
            CronUtil.schedule(autoTestConfig.getCronExpression(), (Task) () -> {
                try {
                    lock.lock();
                    // parse input to commands list
                    List<Commands> commands = parseCommandsList(autoTestConfig);
                    // run the commands list
                    runCommandsList(commands, autoTestConfig, variablesMap);
                } finally {
                    lock.unlock();
                }
            });
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        } else {
            // parse input to commands list
            List<Commands> commands = parseCommandsList(autoTestConfig);
            // run the commands list
            runCommandsList(commands, autoTestConfig, variablesMap);
        }
    }

    public void execute(AutoTestConfig autoTestConfig) {
        Dict variablesMap = new Dict();
        this.execute(autoTestConfig, variablesMap);
    }

    public void execute(String mode, String path) {
        // auto test config
        AutoTestConfig autoTestConfig = new AutoTestConfig();
        autoTestConfig.setTestMode(mode);
        autoTestConfig.setTestCasePath(path);
        autoTestConfig.readConfigurations();
        this.execute(autoTestConfig);
    }

    public void execute(String mode, String path, String testCases) {
        // auto test config
        AutoTestConfig autoTestConfig = new AutoTestConfig();
        autoTestConfig.setTestMode(mode);
        autoTestConfig.setTestCasePath(path);
        autoTestConfig.setTestCases(testCases);
        autoTestConfig.readConfigurations();
        this.execute(autoTestConfig);
    }

    /**
     * method to parse input to commands list
     */
    private List<Commands> parseCommandsList(AutoTestConfig autoTestConfig) {
        List<Commands> commandsList = new ArrayList<>();
        // get the test case path
        String testCasePath = autoTestConfig.getTestCasePath();
        if (!FileUtil.exist(testCasePath)) {
            testCasePath = autoTestConfig.getWorkDir().concat(testCasePath);
            if (!FileUtil.exist(testCasePath)) {
                throw new CommandExecuteException("test case file not exist");
            }
        }
        log.info("test case path = {} ", testCasePath);
        switch (autoTestConfig.getTestMode()) {
            case "xlsx":
                List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(testCasePath);
                List<String> testCasesArray = new ArrayList<>();
                if (StrUtil.isNotEmpty(autoTestConfig.getTestCases())) {
                    String[] testCaseArray = autoTestConfig.getTestCases().split(",");
                    getTestCases(testCasesArray, testCaseArray);
                }
                if (CollUtil.isNotEmpty(testCasesArray)) {
                    for (Commands fromXlsx : commandsFromXlsx) {
                        if (testCasesArray.contains(fromXlsx.getCaseId())) {
                            commandsList.add(fromXlsx);
                        }
                    }
                } else {
                    commandsList.addAll(commandsFromXlsx);
                }
                break;
            case "csv":
                commandsList.addAll(CommandParserUtil.getCommandsFromCsv(testCasePath));
                break;
            case "json":
                commandsList.addAll(CommandParserUtil.getCommandsFromJson(testCasePath));
                // save commandsList to a xlsx file
                CommandParserUtil.saveCommandsListToXlsx(commandsList, autoTestConfig.getFileDownloadPath());
                break;
            default:
                throw new RuntimeException("test mode not supported");
        }
        return commandsList;
    }

    /**
     * get test cases
     *
     * @param testCasesArray test cases array for result
     * @param testCaseArray  test case array
     */
    public static void getTestCases(List<String> testCasesArray, String[] testCaseArray) {
        for (String testCase : testCaseArray) {
            if (testCase.indexOf("-") > 0) {
                String[] testCaseRange = testCase.split("-");
                int start = Integer.parseInt(testCaseRange[0]);
                int end = Integer.parseInt(testCaseRange[1]);
                for (int i = start; i <= end; i++) {
                    testCasesArray.add(String.valueOf(i));
                }
            } else {
                testCasesArray.add(testCase);
            }
        }
    }

    /**
     * method to execute the commands
     */
    public void runCommandsList(List<Commands> commandsList, AutoTestConfig autoTestConfig, Dict variablesMap) {
        // check if there is web command
        boolean isWebCommand = false;
        for (Commands commands : commandsList) {
            // init the executor
            if (commands.isWebCommand()) {
                isWebCommand = true;
                break;
            }
        }
        // init the executor
        if (!autoTestConfig.isRestartWebDriverByCase() && isWebCommand) {
            this.init(autoTestConfig);
        }
        // clear result file
        FileUtil.writeUtf8String("", autoTestConfig.getBaseDir() + "/result.txt");

        // execute the commands
        for (Commands commands : commandsList) {
            try {
                // init the executor
                if (autoTestConfig.isRestartWebDriverByCase() && isWebCommand) {
                    this.init(autoTestConfig);
                }
                variablesMap.set("caseId", commands.getCaseId());
                this.executeCommands(commands, autoTestConfig, variablesMap);
            } finally {
                // destroy the executor
                if (autoTestConfig.isRestartWebDriverByCase() && isWebCommand) {
                    this.destroy();
                }
            }
        }
        // destroy the executor
        if (!autoTestConfig.isRestartWebDriverByCase() && isWebCommand) {
            this.destroy();
        }
    }

    /**
     * destroy the executor
     */
    private void destroy() {
        WebDriverRunner.closeWebDriver();
    }

    /**
     * method to execute the commands
     */
    public void executeCommands(Commands commands, AutoTestConfig autoTestConfig, Dict variablesMap) {
        boolean result;
        // loop through the commands
        for (ICommand command : commands.getCommands()) {
            try {
                // execute the command
                log.info("execute command: " + command);
                if (!command.isSkip()) {
                    command.setVariableMap(variablesMap);
                    command.setAutoTestConfig(autoTestConfig);
                    result = command.execute();
                    if (!result) {
                        // write failed result to file
                        writeResultToFile("caseId: " + commands.getCaseId() + " result: false\n", autoTestConfig.getBaseDir() + "/result.txt");
                        log.error("execute command failed, command={}", command);
                        return;
                    }
                }
            } catch (Exception e) {
                // write failed result to file
                writeResultToFile("caseId: " + commands.getCaseId() + " result: false\n", autoTestConfig.getBaseDir() + "/result.txt");
                log.error("execute command failed, command={}", command);
                log.error("execute command failed detail", e);
                return;
            }
        }

        // stop recorder
        RecorderCommand.stop();

        // write success result to file
        writeResultToFile("caseId: " + commands.getCaseId() + " result: true\n", autoTestConfig.getBaseDir() + "/result.txt");
        log.info(variablesMap.toString());
    }

    // function to write the result to a file with append mode
    public void writeResultToFile(String result, String filePath) {
        FileUtil.appendUtf8String(result, filePath);
    }
}
