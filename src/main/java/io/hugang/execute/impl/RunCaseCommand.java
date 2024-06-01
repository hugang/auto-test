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

/**
 * 用于调用子命令集
 */
public class RunCaseCommand extends Command {
    public RunCaseCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        List<Commands> subCommandsList;
        String path = this.getFilePath(this.getTarget());
        // 获取子命令集类型
        String type = this.getDictStr("type", "xlsx");
        // 可以指定执行的行id
        String testCase = this.getDictStr("value", null);
        switch (type) {
            case "xlsx":
                subCommandsList = CommandParserUtil.getCommandsFromXlsx(path);
                List<String> testCasesArray = new ArrayList<>();
                List<Commands> executeCommands = new ArrayList<>();
                if (StrUtil.isNotEmpty(testCase)) {
                    String[] testCaseArray = testCase.split(",");
                    testCasesArray.addAll(BasicExecutor.getTestCases(testCaseArray));
                }
                if (CollUtil.isNotEmpty(testCasesArray)) {
                    for (Commands fromXlsx : subCommandsList) {
                        if (testCasesArray.contains(fromXlsx.getCaseId())) {
                            executeCommands.add(fromXlsx);
                        }
                    }
                }
                if (CollUtil.isNotEmpty(executeCommands)) {
                    subCommandsList = executeCommands;
                }
                break;
            case "csv":
                subCommandsList = CommandParserUtil.getCommandsFromCsv(path);
                break;
            case "json":
                subCommandsList = CommandParserUtil.getCommandsFromJson(path);
                break;
            default:
                throw new CommandExecuteException("not support type: " + type);
        }

        for (Commands commands : subCommandsList) {
            for (ICommand command : commands.getCommands()) {
                if (!command.execute()) {
                    return false;
                }
            }
        }
        return true;
    }
}
