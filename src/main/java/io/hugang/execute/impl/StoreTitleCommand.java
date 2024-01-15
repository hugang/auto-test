package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class StoreTitleCommand extends Command {

    public StoreTitleCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), WebDriverRunner.getWebDriver().getTitle());
        return true;
    }
}
