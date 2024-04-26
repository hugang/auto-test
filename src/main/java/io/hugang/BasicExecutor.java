package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.config.AutoTestConfig;
import io.hugang.exceptions.AutoTestException;
import io.hugang.execute.Commands;
import io.hugang.execute.ICommand;
import io.hugang.execute.ext.RecorderCommand;
import io.hugang.util.CommandParserUtil;
import io.hugang.util.ThreadContext;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * main executor class
 *
 * @author hugang
 */
public class BasicExecutor {
    private static final Log log = Log.get();


    public BasicExecutor() {
    }

    /**
     * initialize web driver
     */
    public void init() {
        // create a web driver by webDriver
        Configuration.browser = ThreadContext.getAutoTestConfig().getWebDriverPath();
        // set the download path
        Configuration.reportsFolder = ThreadContext.getAutoTestConfig().getFileDownloadPath();
        // store the browser options
        Map<String, Object> optionsMap = new HashMap<>();
        // set pdf file always open externally
        optionsMap.put("plugins.always_open_pdf_externally", true);
        // set the download path
        optionsMap.put("download.default_directory", ThreadContext.getAutoTestConfig().getFileDownloadPath());
        // set browser size
        Dimension targetWindowSize = new Dimension(ThreadContext.getAutoTestConfig().getWidth(), ThreadContext.getAutoTestConfig().getHeight());

        WebDriver driver;
        // create chrome webdriver
        switch (ThreadContext.getAutoTestConfig().getWebDriverName()) {
            case "chrome": {
                System.setProperty("webdriver.chrome.driver", ThreadContext.getAutoTestConfig().getWebDriverPath());
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--user-data-dir=" + ThreadContext.getAutoTestConfig().getUserProfilePath());
                // binary path
                if (ObjectUtil.isNotEmpty(ThreadContext.getAutoTestConfig().getBrowserBinaryPath())) {
                    options.setBinary(ThreadContext.getAutoTestConfig().getBrowserBinaryPath());
                }
                options.addArguments("--remote-allow-origins=*");
                options.setExperimentalOption("prefs", optionsMap);
                driver = new ChromeDriver(options);
                break;
            }
            // create edge webdriver
            case "edge": {
                System.setProperty("webdriver.edge.driver", ThreadContext.getAutoTestConfig().getWebDriverPath());
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--user-data-dir=" + ThreadContext.getAutoTestConfig().getUserProfilePath());
                // binary path
                if (ObjectUtil.isNotEmpty(ThreadContext.getAutoTestConfig().getBrowserBinaryPath())) {
                    options.setBinary(ThreadContext.getAutoTestConfig().getBrowserBinaryPath());
                }
                options.addArguments("--remote-allow-origins=*");
                options.setExperimentalOption("prefs", optionsMap);
                driver = new EdgeDriver(options);
                break;
            }
            // create firefox webdriver
            case "firefox": {
                System.setProperty("webdriver.gecko.driver", ThreadContext.getAutoTestConfig().getWebDriverPath());
                FirefoxOptions options = new FirefoxOptions();
                // binary path
                if (ObjectUtil.isNotEmpty(ThreadContext.getAutoTestConfig().getBrowserBinaryPath())) {
                    options.setBinary(ThreadContext.getAutoTestConfig().getBrowserBinaryPath());
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
    List<Commands> execute() {
        AtomicReference<List<Commands>> commands = new AtomicReference<>();
        if (ThreadContext.getAutoTestConfig().isCronEnabled()) {
            ReentrantLock lock = new ReentrantLock();
            CronUtil.schedule(ThreadContext.getAutoTestConfig().getCronExpression(), (Task) () -> {
                try {
                    lock.lock();
                    // parse input to commands list
                    commands.set(parseCommandsList());
                    // execute the commands
                    runCommandsList(commands.get());
                } finally {
                    lock.unlock();
                }
            });
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        } else {
            // parse input to commands list
            commands.set(parseCommandsList());
            // execute the commands
            runCommandsList(commands.get());
        }
        return commands.get();
    }

    public List<Commands> execute(String mode, String path) {
        // auto test config
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();
        autoTestConfig.setTestMode(mode);
        autoTestConfig.setTestCasePath(path);
        autoTestConfig.readConfigurations();
        ThreadContext.setAutoTestConfig(autoTestConfig);
        return this.execute();
    }

    public List<Commands> execute(String mode, String path, String testCases) {
        // auto test config
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();
        autoTestConfig.setTestMode(mode);
        autoTestConfig.setTestCasePath(path);
        autoTestConfig.setTestCases(testCases);
        autoTestConfig.readConfigurations();
        ThreadContext.setAutoTestConfig(autoTestConfig);
        return this.execute();
    }

    /**
     * method to parse input to commands list
     */
    private List<Commands> parseCommandsList() {
        List<Commands> commandsList = new ArrayList<>();
        // get the test case path
        String testCasePath = ThreadContext.getAutoTestConfig().getTestCasePath();
        if (!FileUtil.exist(testCasePath)) {
            testCasePath = ThreadContext.getAutoTestConfig().getBaseDir().concat(testCasePath);
            if (!FileUtil.exist(testCasePath)) {
                throw new AutoTestException("test case file not exist");
            }
        }
        log.info("test case path = {} ", testCasePath);
        switch (ThreadContext.getAutoTestConfig().getTestMode()) {
            case "xlsx":
                List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(testCasePath);
                List<String> testCasesArray = new ArrayList<>();
                if (StrUtil.isNotEmpty(ThreadContext.getAutoTestConfig().getTestCases())) {
                    String[] testCaseArray = ThreadContext.getAutoTestConfig().getTestCases().split(",");
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
                CommandParserUtil.saveCommandsListToXlsx(commandsList, ThreadContext.getAutoTestConfig().getFileDownloadPath());
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
    public void runCommandsList(List<Commands> commandsList) {
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
        if (!ThreadContext.getAutoTestConfig().isRestartWebDriverByCase() && isWebCommand) {
            this.init();
        }
        // execute the commands
        for (Commands commands : commandsList) {
            try {
                // init the executor
                if (ThreadContext.getAutoTestConfig().isRestartWebDriverByCase() && isWebCommand) {
                    this.init();
                }
                ThreadContext.getVariables().set("caseId", commands.getCaseId());
                this.executeCommands(commands);
            } finally {
                // destroy the executor
                if (ThreadContext.getAutoTestConfig().isRestartWebDriverByCase() && isWebCommand) {
                    this.destroy();
                }
            }
        }
        // destroy the executor
        if (!ThreadContext.getAutoTestConfig().isRestartWebDriverByCase() && isWebCommand) {
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
    public void executeCommands(Commands commands) {
        boolean result;
        // loop through the commands
        for (ICommand command : commands.getCommands()) {
            try {
                // execute the command
                log.info("execute command: " + command);
                if (!command.isSkip()) {
                    result = command.execute();
                    if (!result) {
                        // write failed result to file
                        log.error("execute command failed, command={}", command);
                    }
                }
            } catch (AutoTestException e) {
                // write failed result to file
                log.error("execute command failed, command={}", command);
                log.error("execute command failed detail", e);
                break;
            }
        }

        // stop recorder
        RecorderCommand.stop();

        // log the variables
        log.info(ThreadContext.getVariables().toString());
    }
}
