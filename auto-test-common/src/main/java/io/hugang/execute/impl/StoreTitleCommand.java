package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class StoreTitleCommand extends Command {


    @Override
    public boolean execute() {
        CommandExecuteUtil.setVariable(this.getValue(), WebDriverRunner.getWebDriver().getTitle());
        return true;
    }
}