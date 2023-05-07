package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.bean.Commands;
import io.hugang.config.AutoTestConfig;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import io.hugang.execute.CommandExecutorFactory;
import io.hugang.parse.CommandParserUtil;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.sleep;

/**
 * main executor class
 *
 * @author hugang
 */
public class BasicExecutor {
    private static final Log log = LogFactory.get();
    // commands list
    private List<Commands> commandsList;
    // auto test config
    private final AutoTestConfig autoTestConfig = new AutoTestConfig();
    // map to storage variables
    public static final Map<String, String> variablesMap = new HashMap<>();

    public BasicExecutor() {
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
        if (autoTestConfig.getWebDriverName().equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", autoTestConfig.getWebDriverPath());
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
            options.addArguments("--remote-allow-origins=*");
            options.setExperimentalOption("prefs", optionsMap);
            driver = new ChromeDriver(options);
        }
        // create edge webdriver
        else if (autoTestConfig.getWebDriverName().equals("edge")) {
            System.setProperty("webdriver.edge.driver", autoTestConfig.getWebDriverPath());
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--user-data-dir=" + autoTestConfig.getUserProfilePath());
            options.setExperimentalOption("prefs", optionsMap);
            driver = new EdgeDriver(options);
        } else {
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
        // read the user properties
        autoTestConfig.readUserProperties();
        // prepare work directories
        this.prepareWorkDirectories();

        // read csv from xlsx when xlsx file path is not null
        switch (autoTestConfig.getTestMode()) {
            case "xlsx":
                List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(autoTestConfig.getTestCasePath());
                List<String> testCasesArray = new ArrayList<>();
                if (StrUtil.isNotEmpty(autoTestConfig.getTestCases())) {
                    String[] testCaseArray = autoTestConfig.getTestCases().split(",");
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
                if (CollUtil.isNotEmpty(testCasesArray)) {
                    this.setCommandsList(new ArrayList<>());
                    for (Commands fromXlsx : commandsFromXlsx) {
                        if (testCasesArray.contains(fromXlsx.getCaseId())) {
                            this.getCommandsList().add(fromXlsx);
                        }
                    }
                } else {
                    this.setCommandsList(commandsFromXlsx);
                }
                break;
            case "csv":
                this.setCommandsList(CommandParserUtil.getCommandsFromCsv(autoTestConfig.getTestCasePath()));
                break;
            case "side":
                this.setCommandsList(CommandParserUtil.getCommandsFromSide(autoTestConfig.getTestCasePath()));
                // save commandsList to a xlsx file
                CommandParserUtil.saveCommandsListToXlsx(this.getCommandsList(), autoTestConfig.getFileDownloadPath());
                break;
            default:
                throw new RuntimeException("test mode not supported");
        }

        // execute the commands
        for (Commands commands : this.getCommandsList()) {
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
        for (Command command : commands.getCommands()) {
            // get the command executor
            CommandExecutor executor = CommandExecutorFactory.getExecutor(command.getCommand());
            // execute the command
            log.info("execute command: " + command);
            if (executor != null) {
                result = executor.execute(command);
            } else {
                throw new RuntimeException("command not supported");
            }
            if (!result) {
                throw new RuntimeException("execute command failed");
            }
        }
    }

    public List<Commands> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(List<Commands> commandsList) {
        this.commandsList = commandsList;
    }

}
