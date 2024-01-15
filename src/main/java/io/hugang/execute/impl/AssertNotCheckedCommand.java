package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertNotCheckedCommand extends Command {
    public AssertNotCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        try {
            String target = this.getTarget();
            return !CommandExecuteUtil.getElement(target).isSelected();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
