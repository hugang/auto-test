package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreTextCommand extends Command {


    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), CommandExecuteUtil.getElement(this.getTarget()).getText());
        return true;
    }
}
