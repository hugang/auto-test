package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertNotCheckedCommand extends Command {
    public AssertNotCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            return !CommandExecuteUtil.getElement(this.getTarget()).isSelected();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
