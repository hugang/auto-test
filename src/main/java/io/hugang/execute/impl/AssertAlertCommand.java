package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class AssertAlertCommand extends Command {

    public AssertAlertCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        try {
            String text = WebDriverRunner.getWebDriver().switchTo().alert().getText();
            return text.equals(this.getTarget());
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
