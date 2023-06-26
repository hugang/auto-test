package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

@WebCommand
public class StoreXpathCountCommand extends Command {

    public StoreXpathCountCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), String.valueOf(CommandExecuteUtil.findElements(this.getTarget()).size()));
        return true;
    }
}
