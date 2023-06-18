package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreCommand extends Command {

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), this.getTarget());
        return true;
    }
}
