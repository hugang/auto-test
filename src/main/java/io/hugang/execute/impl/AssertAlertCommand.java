package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class AssertAlertCommand extends Command {
    public AssertAlertCommand(OriginalCommand originalCommand) {
        super(originalCommand);
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
