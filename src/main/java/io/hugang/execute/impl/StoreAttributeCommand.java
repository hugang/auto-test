package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class StoreAttributeCommand extends Command {

    public StoreAttributeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String targetStr = this.getTarget();
        int lastIndexOf = targetStr.lastIndexOf("@");
        String target = targetStr.substring(0, lastIndexOf);
        String attribute = targetStr.substring(lastIndexOf + 1);
        SelenideElement $ = CommandExecuteUtil.getElement(target);
        this.setVariable(this.getDictStr("value", this.getValue()), $.getAttribute(attribute));
        return true;
    }
}
