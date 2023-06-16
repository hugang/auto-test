package io.hugang.execute.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.Commands;
import io.hugang.bean.ICommand;
import io.hugang.parse.CommandParserUtil;

import java.util.ArrayList;
import java.util.List;

public class RunCaseCommand extends Command {
    public RunCaseCommand() {
    }

    public RunCaseCommand(String command, String target) {
        super(command, target);
    }

    public RunCaseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public RunCaseCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        List<Commands> commandsFromXlsx;
        String[] split = this.getTarget().split(",");
        String type = split[0];
        String path = split[1];
        switch (type) {
            case "xlsx":
                commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(path);
                List<String> testCasesArray = new ArrayList<>();
                List<Commands> executeCommands = new ArrayList<>();
                if (StrUtil.isNotEmpty(this.getValue())) {
                    String[] testCaseArray = this.getValue().split(",");
                    BasicExecutor.getTestCases(testCasesArray, testCaseArray);
                }
                if (CollUtil.isNotEmpty(testCasesArray)) {
                    for (Commands fromXlsx : commandsFromXlsx) {
                        if (testCasesArray.contains(fromXlsx.getCaseId())) {
                            executeCommands.add(fromXlsx);
                        }
                    }
                }
                if (CollUtil.isNotEmpty(executeCommands)) {
                    commandsFromXlsx = executeCommands;
                }
                break;
            case "csv":
                commandsFromXlsx = CommandParserUtil.getCommandsFromCsv(path);
                break;
            default:
                throw new CommandExecuteException("not support type: " + type);
        }

        for (Commands commands : commandsFromXlsx) {
            for (ICommand command : commands.getCommands()) {
                if (!command.execute()) {
                    return false;
                }
            }
        }
        return true;
    }
}
