package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class StoreTitle implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "storeTitle";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.setVariable(command.getValue(), WebDriverRunner.getWebDriver().getTitle());
        return true;
    }
}
