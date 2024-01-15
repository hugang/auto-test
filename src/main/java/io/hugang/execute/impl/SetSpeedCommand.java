package io.hugang.execute.impl;

import io.hugang.execute.Command;

public class SetSpeedCommand extends Command {

    public SetSpeedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        this.setVariable("setSpeed", this.render(this.getTarget()));
        return true;
    }
}
