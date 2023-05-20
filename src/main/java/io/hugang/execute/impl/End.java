package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import io.hugang.execute.CommandExecutorFactory;

public class End implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "end";
    }

    @Override
    public boolean execute(Command command) {
        int times = CommandExecuteUtil.getTimes();

        while (times > 1) {
            for (Command timesCommand : CommandExecuteUtil.getTimesCommands()) {
                CommandExecutor executor = CommandExecutorFactory.getExecutor(timesCommand.getCommand());
                executor.execute(timesCommand);
            }
            times--;
        }
        CommandExecuteUtil.clearTimes();
        return true;
    }
}
