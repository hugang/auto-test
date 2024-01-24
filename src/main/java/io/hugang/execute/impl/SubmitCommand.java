package io.hugang.execute.impl;

import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class SubmitCommand extends Command {
    public SubmitCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        try {
            CommandExecuteUtil.getElement(this.getTarget()).submit();
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
