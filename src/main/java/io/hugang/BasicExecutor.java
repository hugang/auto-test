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
import io.hugang.util.CommandExecuteUtil;
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
 * 主程序入口:
 * 1 通过命令行启动 see {@link RunAutoTest}
 * 2 通过WEB API调用 see {@link io.hugang.server.AutoTestServer}
 *
 * @author hugang
 */
public class BasicExecutor {
    private static final Log log = Log.get();
    // 控制同时执行1个driver
    private static final ReentrantLock lock = new ReentrantLock();

    private BasicExecutor() {
    }

    public static BasicExecutor create() {
        return new BasicExecutor();
    }

    /**
     * initialize web driver
     */
    public void init() {
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();
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
                if (StrUtil.isNotEmpty(autoTestConfig.getUserProfilePath())) {
                    options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
                }
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
                if (StrUtil.isNotEmpty(autoTestConfig.getUserProfilePath())) {
                    options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
                }
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
     * execute the commands
     */
    public List<Commands> execute() {

        AtomicReference<List<Commands>> commands = new AtomicReference<>();
        if (ThreadContext.getAutoTestConfig().isCronEnabled()) {
            CronUtil.schedule(ThreadContext.getAutoTestConfig().getCronExpression(), (Task) () -> {
                // parse input to commands list
                commands.set(parseCommandsList());
                // execute the commands
                runCommandsList(commands.get());
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
     * parse input to commands list
     */
    private List<Commands> parseCommandsList() {
        List<Commands> commandsList = new ArrayList<>();
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();
        // get the test case path
        String testCasePath = autoTestConfig.getTestCasePath();
        if (!FileUtil.exist(testCasePath)) {
            testCasePath = CommandExecuteUtil.getFilePathWithBaseDir(testCasePath);
            if (!FileUtil.exist(testCasePath)) {
                throw new AutoTestException("test case file not exist");
            }
        }
        log.info("test case path = {} ", testCasePath);
        switch (autoTestConfig.getTestMode()) {
            case "xlsx":
                List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(testCasePath);
                List<String> testCasesArray = new ArrayList<>();
                if (StrUtil.isNotEmpty(autoTestConfig.getTestCases())) {
                    String[] testCaseArray = autoTestConfig.getTestCases().split(",");
                    testCasesArray.addAll(getTestCases(testCaseArray));
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
     * @param testCaseArray test case array
     * @return test cases
     */
    public static List<String> getTestCases(String[] testCaseArray) {
        List<String> testCasesArray = new ArrayList<>();
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
        return testCasesArray;
    }

    /**
     * execute the commands
     */
    public void runCommandsList(List<Commands> commandsList) {
        try {
            // only one thread can execute the commands
            lock.lock();
            AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();

            // check if there is web command
            // boolean isWebCommand = false;
            // for (Commands commands : commandsList) {
            //     // init the executor
            //     if (commands.isWebCommand()) {
            //         isWebCommand = true;
            //         break;
            //     }
            // }
            // init the executor
            if (!autoTestConfig.isRestartWebDriverByCase()) {
                this.init();
            }
            // execute the commands
            for (Commands commands : commandsList) {
                try {
                    // init the executor
                    if (autoTestConfig.isRestartWebDriverByCase()) {
                        this.init();
                    }
                    ThreadContext.getVariables().set("caseId", commands.getCaseId());
                    this.executeCommands(commands);
                } finally {
                    // destroy the executor
                    if (autoTestConfig.isRestartWebDriverByCase()) {
                        this.destroy();
                    }
                }
            }
            // destroy the executor
            if (!autoTestConfig.isRestartWebDriverByCase()) {
                this.destroy();
            }
        } finally {
            lock.unlock();
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
        //RecorderCommand.stop();

        // log the variables
        log.info(ThreadContext.getVariables().toString());
    }
}
