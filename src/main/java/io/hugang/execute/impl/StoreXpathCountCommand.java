package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreXpathCountCommand extends Command {

    public StoreXpathCountCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), String.valueOf(CommandExecuteUtil.findElements(this.getTarget()).size()));
        return true;
    }
}
