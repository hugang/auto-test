package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreJsonCommand extends Command {

    public StoreJsonCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String target = this.getTarget();
        String value = this.getDictStr("value", this.getValue());
        CommandExecuteUtil.setVariable(value, target);
        return true;
    }
}
