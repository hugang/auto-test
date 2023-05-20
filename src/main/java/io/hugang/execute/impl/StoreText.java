package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreText implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeText";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), CommandExecuteUtil.getElement(command.getTarget()).getText());
        return true;
    }
}
