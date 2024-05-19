package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class SetSpeedCommand extends Command {
    public SetSpeedCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        this.setVariable("setSpeed", this.render(this.getTarget()));
        return true;
    }
}
