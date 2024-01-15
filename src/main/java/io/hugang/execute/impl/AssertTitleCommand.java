package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class AssertTitleCommand extends Command {

    public AssertTitleCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        return WebDriverRunner.getWebDriver().getTitle().equals(render(this.getTarget()));
    }
}
