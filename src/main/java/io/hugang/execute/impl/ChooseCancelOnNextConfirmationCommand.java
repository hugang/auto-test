package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.execute.Command;

public class ChooseCancelOnNextConfirmationCommand extends Command {
    public ChooseCancelOnNextConfirmationCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        WebDriverRunner.getWebDriver().switchTo().alert().dismiss();
        return true;
    }

}
