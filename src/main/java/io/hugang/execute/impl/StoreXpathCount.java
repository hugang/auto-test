package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreXpathCount implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeXpathCount";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), String.valueOf(CommandExecuteUtil.findElements(command.getTarget()).size()));
        return true;
    }
}
