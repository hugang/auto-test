package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreValue implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeValue";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), CommandExecuteUtil.getElement(command.getTarget()).getAttribute("value"));
        return true;
    }
}
