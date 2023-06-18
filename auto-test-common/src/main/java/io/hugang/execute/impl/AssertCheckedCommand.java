package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertCheckedCommand extends Command {

    public AssertCheckedCommand() {
    }

    public AssertCheckedCommand(String command, String target) {
        super(command, target);
    }

    public AssertCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertCheckedCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        try {
            return CommandExecuteUtil.getElement(this.getTarget()).isSelected();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
