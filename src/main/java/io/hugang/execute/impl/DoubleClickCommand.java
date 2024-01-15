package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class DoubleClickCommand extends Command {
    public DoubleClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        CommandExecuteUtil.getElement(this.getTarget()).doubleClick();
        return true;
    }
}
