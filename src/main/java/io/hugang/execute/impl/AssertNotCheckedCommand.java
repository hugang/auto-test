package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertNotCheckedCommand extends Command {

    public AssertNotCheckedCommand() {
    }

    public AssertNotCheckedCommand(String command, String target) {
        super(command, target);
    }

    public AssertNotCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertNotCheckedCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
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
