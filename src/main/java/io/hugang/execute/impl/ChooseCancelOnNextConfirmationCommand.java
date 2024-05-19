package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

@WebCommand
public class ChooseCancelOnNextConfirmationCommand extends Command {
    public ChooseCancelOnNextConfirmationCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        WebDriverRunner.getWebDriver().switchTo().alert().dismiss();
        return true;
    }

}
