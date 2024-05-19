package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class StoreJsonCommand extends Command {
    public StoreJsonCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        String value = this.getDictStr("value", this.getValue());
        this.setVariable(value, target);
        return true;
    }
}
