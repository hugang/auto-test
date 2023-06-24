package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class DoubleClickCommand extends Command {
    public DoubleClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.getElement(this.getTarget()).doubleClick();
        return true;
    }
}
