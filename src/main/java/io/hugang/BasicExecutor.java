package io.hugang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.log.Log;
import io.hugang.config.AutoTestConfig;
import io.hugang.exceptions.AutoTestException;
import io.hugang.execute.Commands;
import io.hugang.execute.ICommand;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.CommandParserUtil;
import io.hugang.util.ThreadContext;
import io.hugang.util.WebDriverUtil;

import java.util.ArrayList;
import java.util.List;
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
    public static final WebDriverUtil webDriverUtil = new WebDriverUtil();

    private BasicExecutor() {
    }

    public static BasicExecutor create() {
        return new BasicExecutor();
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
                        ThreadContext.remove("__isWebCommand__");
                        webDriverUtil.destroy();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
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
