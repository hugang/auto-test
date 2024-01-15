package io.hugang.execute.impl;

import io.hugang.execute.Command;

public class StoreCommand extends Command {

    public StoreCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String value = this.getDictStr("value", this.getValue());
        this.setVariable(this.render(value), this.render(this.getTarget()));
        return true;
    }
}
