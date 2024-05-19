package io.hugang.execute.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.hugang.BasicExecutor;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.Commands;
import io.hugang.execute.ICommand;
import io.hugang.util.CommandParserUtil;

import java.util.ArrayList;
import java.util.List;

public class RunCaseCommand extends Command {
    public RunCaseCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        List<Commands> commandsFromXlsx;
        String path = this.getFilePath(this.getTarget());
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
