package io.hugang.execute.impl;

import io.hugang.execute.Command;

public class StoreJsonCommand extends Command {

    public StoreJsonCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        String value = this.getDictStr("value", this.getValue());
        this.setVariable(value, target);
        return true;
    }
}
