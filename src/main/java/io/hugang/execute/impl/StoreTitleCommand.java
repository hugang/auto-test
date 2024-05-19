package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

@WebCommand
public class StoreTitleCommand extends Command {
    public StoreTitleCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        this.setVariable(this.getDictStr("value", this.getValue()), WebDriverRunner.getWebDriver().getTitle());
        return true;
    }
}
