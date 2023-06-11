package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class SubmitCommand extends Command {
    public SubmitCommand() {
    }

    public SubmitCommand(String command, String target) {
        super(command, target);
    }

    public SubmitCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public SubmitCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            CommandExecuteUtil.getElement(this.getTarget()).submit();
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
