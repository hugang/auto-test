package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

@WebCommand
public class StoreTitleCommand extends Command {

    public StoreTitleCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), WebDriverRunner.getWebDriver().getTitle());
        return true;
    }
}
