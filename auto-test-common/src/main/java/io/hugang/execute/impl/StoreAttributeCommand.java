package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

@WebCommand
public class StoreAttributeCommand extends Command {

    public StoreAttributeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        int lastIndexOf = this.getTarget().lastIndexOf("@");
        String target = this.getTarget().substring(0, lastIndexOf);
        String attribute = this.getTarget().substring(lastIndexOf + 1);
        SelenideElement $ = CommandExecuteUtil.getElement(target);
        CommandExecuteUtil.setVariable(this.getValue(), $.getAttribute(attribute));
        return true;
    }
}
