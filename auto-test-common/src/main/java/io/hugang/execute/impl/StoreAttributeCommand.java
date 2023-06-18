package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreAttributeCommand extends Command {


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
