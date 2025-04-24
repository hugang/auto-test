package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
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

import java.net.Authenticator;
import java.net.PasswordAuthentication;
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
     * Initialize the web driver with configurations from AutoTestConfig.
     */
    public void init() {
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();

        validateWebDriverPath(autoTestConfig);
        configureProxySettings(autoTestConfig);
        configureSelenideSettings(autoTestConfig);

        WebDriver driver = initializeWebDriver(autoTestConfig);
        configureBrowserWindow(driver, autoTestConfig);

        WebDriverRunner.setWebDriver(driver);
    }

    /**
     * Validate the WebDriver path.
     */
    private void validateWebDriverPath(AutoTestConfig autoTestConfig) {
        if (StrUtil.isEmpty(autoTestConfig.getWebDriverPath())) {
            throw new AutoTestException("WebDriver path is not configured or empty");
        }
        if (!FileUtil.exist(autoTestConfig.getWebDriverPath())) {
            throw new AutoTestException("Invalid WebDriver path or file does not exist: " + autoTestConfig.getWebDriverPath());
        }
    }

    /**
     * Configure proxy settings if provided.
     */
    private void configureProxySettings(AutoTestConfig autoTestConfig) {
        if (StrUtil.isNotEmpty(autoTestConfig.getProxyHost()) && StrUtil.isNotEmpty(String.valueOf(autoTestConfig.getProxyPort()))) {
            System.setProperty("http.proxyHost", autoTestConfig.getProxyHost());
            System.setProperty("http.proxyPort", String.valueOf(autoTestConfig.getProxyPort()));
            System.setProperty("https.proxyHost", autoTestConfig.getProxyHost());
            System.setProperty("https.proxyPort", String.valueOf(autoTestConfig.getProxyPort()));

            if (StrUtil.isNotEmpty(autoTestConfig.getProxyUser()) && StrUtil.isNotEmpty(autoTestConfig.getProxyPassword())) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(autoTestConfig.getProxyUser(), autoTestConfig.getProxyPassword().toCharArray());
                    }
                });
            }
        }
    }

    /**
     * Configure Selenide settings.
     */
    private void configureSelenideSettings(AutoTestConfig autoTestConfig) {
        Configuration.browser = autoTestConfig.getWebDriverPath();
        Configuration.reportsFolder = autoTestConfig.getFileDownloadPath();
        Configuration.savePageSource = false;
    }

    /**
     * Initialize the appropriate WebDriver based on the configuration.
     */
    private WebDriver initializeWebDriver(AutoTestConfig autoTestConfig) {
        Map<String, Object> optionsMap = new HashMap<>();
        optionsMap.put("plugins.always_open_pdf_externally", true);
        optionsMap.put("download.default_directory", autoTestConfig.getFileDownloadPath());

        return switch (autoTestConfig.getWebDriverName()) {
            case "chrome" -> initializeChromeDriver(autoTestConfig, optionsMap);
            case "edge" -> initializeEdgeDriver(autoTestConfig, optionsMap);
            case "firefox" -> initializeFirefoxDriver(autoTestConfig);
            default -> throw new RuntimeException("Unsupported WebDriver");
        };
    }

    /**
     * Configure the browser window size.
     */
    private void configureBrowserWindow(WebDriver driver, AutoTestConfig autoTestConfig) {
        Dimension targetWindowSize = new Dimension(autoTestConfig.getWidth(), autoTestConfig.getHeight());
        driver.manage().window().setSize(targetWindowSize);
    }

    /**
     * Initialize Chrome WebDriver.
     */
    private WebDriver initializeChromeDriver(AutoTestConfig config, Map<String, Object> optionsMap) {
        System.setProperty("webdriver.chrome.driver", config.getWebDriverPath());
        ChromeOptions options = new ChromeOptions();
        if (StrUtil.isNotEmpty(config.getUserProfilePath())) {
            options.addArguments("--user-data-dir=" + config.getUserProfilePath());
        }
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", optionsMap);
        return new ChromeDriver(options);
    }

    /**
     * Initialize Edge WebDriver.
     */
    private WebDriver initializeEdgeDriver(AutoTestConfig config, Map<String, Object> optionsMap) {
        System.setProperty("webdriver.edge.driver", config.getWebDriverPath());
        EdgeOptions options = new EdgeOptions();
        if (StrUtil.isNotEmpty(config.getUserProfilePath())) {
            options.addArguments("--user-data-dir=" + config.getUserProfilePath());
        }
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", optionsMap);
        return new EdgeDriver(options);
    }

    /**
     * Initialize Firefox WebDriver.
     */
    private WebDriver initializeFirefoxDriver(AutoTestConfig config) {
        System.setProperty("webdriver.gecko.driver", config.getWebDriverPath());
        FirefoxOptions options = new FirefoxOptions();
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        return new FirefoxDriver(options);
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
     * Parse input to commands list based on test mode.
     */
    private List<Commands> parseCommandsList() {
        List<Commands> commandsList = new ArrayList<>();
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();
        String testCasePath = validateTestCasePath(autoTestConfig);

        log.info("Test case path = {}", testCasePath);

        switch (autoTestConfig.getTestMode()) {
            case "xlsx":
                commandsList = parseXlsxCommands(autoTestConfig, testCasePath);
                break;
            case "csv":
                commandsList.addAll(CommandParserUtil.getCommandsFromCsv(testCasePath));
                break;
            case "json":
                commandsList.addAll(CommandParserUtil.getCommandsFromJson(testCasePath));
                CommandParserUtil.saveCommandsListToXlsx(commandsList, autoTestConfig.getFileDownloadPath());
                break;
            default:
                throw new RuntimeException("Unsupported test mode");
        }
        return commandsList;
    }

    /**
     * Validate and retrieve the test case path.
     */
    private String validateTestCasePath(AutoTestConfig config) {
        String testCasePath = config.getTestCasePath();
        if (!FileUtil.exist(testCasePath)) {
            testCasePath = CommandExecuteUtil.getFilePathWithBaseDir(testCasePath);
            if (!FileUtil.exist(testCasePath)) {
                throw new AutoTestException("Test case file does not exist");
            }
        }
        return testCasePath;
    }

    /**
     * Parse commands from XLSX file.
     */
    private List<Commands> parseXlsxCommands(AutoTestConfig config, String testCasePath) {
        List<Commands> commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(testCasePath);
        List<String> testCasesArray = new ArrayList<>();
        if (StrUtil.isNotEmpty(config.getTestCases())) {
            String[] testCaseArray = config.getTestCases().split(",");
            testCasesArray.addAll(getTestCases(testCaseArray));
        }
        if (CollUtil.isNotEmpty(testCasesArray)) {
            List<Commands> filteredCommands = new ArrayList<>();
            for (Commands command : commandsFromXlsx) {
                if (testCasesArray.contains(command.getCaseId())) {
                    filteredCommands.add(command);
                }
            }
            return filteredCommands;
        }
        return commandsFromXlsx;
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
            // execute the commands
            for (Commands commands : commandsList) {
                try {
                    ThreadContext.setReportPath(autoTestConfig.getFileDownloadPath());
                    ThreadContext.getVariables().set("caseId", commands.getCaseId());
                    this.executeCommands(commands);
                } finally {
                    // destroy the executor
                    if (ThreadContext.containsKey("__isWebCommand__")) {
                        this.destroy();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * destroy the executor
     */
    private void destroy() {
        // Deprecated WebDriverRunner.closeWebDriver();
        Selenide.closeWebDriver();
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
                    // if the command is a web command, and not initialized, then initialize the web driver
                    boolean isWebCommand = command.getClass().isAnnotationPresent(WebCommand.class);
                    if (isWebCommand && !ThreadContext.containsKey("__isWebCommand__") ) {
                        this.init();
                        ThreadContext.setIsWebCommand();
                    }

                    result = command.execute();
                    if (!result) {
                        log.error("execute command failed, command={}", command);
                        throw new AutoTestException("execute command failed, command=" + command);
                    }
                }
            } catch (AutoTestException e) {
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
