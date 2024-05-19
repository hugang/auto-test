package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class SubmitCommand extends Command {
    public SubmitCommand(OriginalCommand originalCommand) {
        super(originalCommand);
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
