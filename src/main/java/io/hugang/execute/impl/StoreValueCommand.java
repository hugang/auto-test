package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreValueCommand extends Command {
    public StoreValueCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), CommandExecuteUtil.getElement(this.getTarget()).getAttribute("value"));
        return true;
    }
}
