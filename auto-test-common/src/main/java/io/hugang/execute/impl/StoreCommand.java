package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreCommand extends Command {

    public StoreCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), this.getTarget());
        return true;
    }
}
