package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreJson implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeJson";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), command.getTarget());
        return true;
    }
}
