package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreAttribute implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeAttribute";
    }

    @Override
    public boolean execute(Command command) {
        int lastIndexOf = command.getTarget().lastIndexOf("@");
        String target = command.getTarget().substring(0, lastIndexOf);
        String attribute = command.getTarget().substring(lastIndexOf + 1);
        SelenideElement $ = CommandExecuteUtil.getElement(target);
        CommandExecuteUtil.setVariable(command.getValue(), $.getAttribute(attribute));
        return true;
    }
}
