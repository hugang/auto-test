package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreXpathCountCommand extends Command {

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), String.valueOf(CommandExecuteUtil.findElements(this.getTarget()).size()));
        return true;
    }
}
