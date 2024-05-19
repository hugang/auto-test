package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreAttributeCommand extends Command {
    public StoreAttributeCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String targetStr = this.getTarget();
        int lastIndexOf = targetStr.lastIndexOf("@");
        String target = targetStr.substring(0, lastIndexOf);
        String attribute = targetStr.substring(lastIndexOf + 1);
        SelenideElement $ = CommandExecuteUtil.getElement(target);
        this.setVariable(this.getDictStr("value", this.getValue()), $.getAttribute(attribute));
        return true;
    }
}
