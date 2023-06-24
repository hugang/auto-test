package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreValueCommand extends Command {

    public StoreValueCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), CommandExecuteUtil.getElement(this.getTarget()).getAttribute("value"));
        return true;
    }
}
