package io.hugang.execute.ext;

import io.hugang.annotation.ReportCommand;
import io.hugang.annotation.WebCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;

@WebCommand
@ReportCommand
public class LogCommand extends Command {
    @Override
    public String getCommand() {
        return "log";
    }

    @Override
    public boolean _execute() throws CommandExecuteException {
        return true;
    }
}
