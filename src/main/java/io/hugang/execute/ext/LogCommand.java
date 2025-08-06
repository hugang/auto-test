package io.hugang.execute.ext;

import io.hugang.annotation.ReportCommand;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

@WebCommand
@ReportCommand
public class LogCommand extends Command {
    public LogCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "log";
    }

    @Override
    public boolean _execute() {
        return true;

    }
}
