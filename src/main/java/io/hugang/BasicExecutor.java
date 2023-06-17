package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
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
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.parse.CommandParserUtil;
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

import static com.codeborne.selenide.Selenide.sleep;

/**
 * main executor class
 *
 * @author hugang
 */
public class BasicExecutor {
    private static final Log log = LogFactory.get();
    // auto test config
    private final AutoTestConfig autoTestConfig = new AutoTestConfig();
    // map to storage variables
    public static final Map<String, String> variablesMap = new HashMap<>();

    public BasicExecutor() {
        // read the user properties
        autoTestConfig.readConfigurations();
        // prepare work directories
        this.prepareWorkDirectories();
    }

    /**
     * initialize
     */
    public void init() {
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
    public void execute() {
        if (autoTestConfig.isCronEnabled()) {
            ReentrantLock lock = new ReentrantLock();
            CronUtil.schedule(autoTestConfig.getCronExpression(), (Task) () -> {
                try {
                    lock.lock();
                    // parse input to commands list
                    List<Commands> commands = parseCommandsList();
                    // run the commands list
                    runCommandsList(commands);
                } finally {
                    lock.unlock();
                }
            });
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        } else {
            // parse input to commands list
            List<Commands> commands = parseCommandsList();
            // run the commands list
            runCommandsList(commands);
        }
    }

    public void execute(String mode, String path) {
        this.autoTestConfig.setTestMode(mode);
        this.autoTestConfig.setTestCasePath(path);
        this.execute();
    }

    /**
     * method to parse input to commands list
     */
    private List<Commands> parseCommandsList() {
        List<Commands> commandsList = new ArrayList<>();
        // read csv from xlsx when xlsx file path is not null
        switch (autoTestConfig.getTestMode()) {
            case "xlsx":
                List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(autoTestConfig.getTestCasePath());
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
                commandsList.addAll(CommandParserUtil.getCommandsFromCsv(autoTestConfig.getTestCasePath()));
                break;
            case "side":
                commandsList.addAll(CommandParserUtil.getCommandsFromSide(autoTestConfig.getTestCasePath()));
                // save commandsList to a xlsx file
                CommandParserUtil.saveCommandsListToXlsx(commandsList, autoTestConfig.getFileDownloadPath());
                break;
            default:
                throw new RuntimeException("test mode not supported");
        }
        return commandsList;
    }

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
    private void runCommandsList(List<Commands> commandsList) {
        // execute the commands
        for (Commands commands : commandsList) {
            // init the executor
            this.init();
            CommandExecuteUtil.setVariable("caseId", commands.getCaseId());
            this.executeCommands(commands);
            sleep(1000);
            // destroy the executor
            this.destroy();
        }
    }

    /**
     * prepare work directories
     */
    private void prepareWorkDirectories() {
        // create the download directory
        FileUtil.mkdir(autoTestConfig.getFileDownloadPath());
        // create the user profile directory
        FileUtil.mkdir(autoTestConfig.getUserProfilePath());
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
            // execute the command
            log.info("execute command: " + command);
            if (!command.execute()) {
                throw new RuntimeException("execute command failed");
            }
        }
        log.info("execute commands success");
    }
}
