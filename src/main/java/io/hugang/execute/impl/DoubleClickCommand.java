package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class DoubleClickCommand extends Command {
    public DoubleClickCommand() {
    }

    public DoubleClickCommand(String command, String target) {
        super(command, target);
    }

    public DoubleClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public DoubleClickCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.getElement(this.getTarget()).doubleClick();
        return true;
    }
}
