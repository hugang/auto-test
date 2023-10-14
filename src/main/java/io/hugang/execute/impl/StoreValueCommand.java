package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreValueCommand extends Command {

    public StoreValueCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), CommandExecuteUtil.getElement(this.getTarget()).getAttribute("value"));
        return true;
    }
}
