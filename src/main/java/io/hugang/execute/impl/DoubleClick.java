package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class DoubleClick implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "doubleClick";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.getElement(command.getTarget()).doubleClick();
        return true;
    }
}
