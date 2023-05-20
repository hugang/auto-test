package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;

public class AssertAlert implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "assertAlert";
    }

    @Override
    public boolean execute(Command command) {
        return WebDriverRunner.getWebDriver().switchTo().alert().getText().equals(command.getValue());
    }
}
