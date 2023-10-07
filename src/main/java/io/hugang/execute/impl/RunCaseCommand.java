package io.hugang.execute.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.Commands;
import io.hugang.bean.ICommand;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.CommandParserUtil;

import java.util.ArrayList;
import java.util.List;

public class RunCaseCommand extends Command {
    public RunCaseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        List<Commands> commandsFromXlsx;
        String path = CommandExecuteUtil.getFilePath(this.getTarget());
        String type = this.getDictStr("type", "xlsx");
        String testCase = this.getDictStr("value", null);
        switch (type) {
            case "xlsx":
                commandsFromXlsx = CommandParserUtil.getCommandsFromXlsx(path);
                List<String> testCasesArray = new ArrayList<>();
                List<Commands> executeCommands = new ArrayList<>();
                if (StrUtil.isNotEmpty(testCase)) {
                    String[] testCaseArray = testCase.split(",");
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
