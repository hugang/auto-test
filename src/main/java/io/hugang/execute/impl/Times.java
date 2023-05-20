package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class Times implements CommandExecutor {

    @Override
    public String getCommandName() {
        return "times";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setTimes(Integer.parseInt(command.getTarget()));
        return true;
    }
}
