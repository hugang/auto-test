package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreTextCommand extends Command {
    public StoreTextCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String target = render(this.getTarget());
        this.setVariable(this.getDictStr("value", this.getValue()), CommandExecuteUtil.getElement(target).getText());
        return true;
    }
}
