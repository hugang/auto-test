package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

@WebCommand
public class SubmitCommand extends Command {
    public SubmitCommand(String command, String target, String value) {
        super(command, target, value);
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
