package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class StoreCommand extends Command {
    public StoreCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String value = this.getDictStr("value", this.getValue());
        this.setVariable(this.render(value), this.render(this.getTarget()));
        return true;
    }
}
