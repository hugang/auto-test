package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class Store implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "store";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), command.getTarget());
        return true;
    }
}
