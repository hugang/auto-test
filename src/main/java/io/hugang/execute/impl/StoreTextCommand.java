package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreTextCommand extends Command {

    public StoreTextCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String target = render(this.getTarget());
        this.setVariable(this.getDictStr("value", this.getValue()), CommandExecuteUtil.getElement(target).getText());
        return true;
    }
}
