package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;

public class AssertAlertCommand extends Command {
    public AssertAlertCommand() {
    }

    public AssertAlertCommand(String command, String target) {
        super(command, target);
    }

    public AssertAlertCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertAlertCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
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
