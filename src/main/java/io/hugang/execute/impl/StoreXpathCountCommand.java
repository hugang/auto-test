package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreXpathCountCommand extends Command {
    public StoreXpathCountCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), String.valueOf(CommandExecuteUtil.findElements(this.getTarget()).size()));
        return true;
    }
}
