package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

@WebCommand
public class AssertTitleCommand extends Command {
    public AssertTitleCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        return WebDriverRunner.getWebDriver().getTitle().equals(render(this.getTarget()));
    }
}
