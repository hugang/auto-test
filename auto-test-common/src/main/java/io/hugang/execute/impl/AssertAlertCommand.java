package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;

public class AssertAlertCommand extends Command {
    public AssertAlertCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            return WebDriverRunner.getWebDriver().switchTo().alert().getText().equals(this.getValue());
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
