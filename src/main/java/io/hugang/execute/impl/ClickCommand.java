package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class ClickCommand extends Command {

    public ClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        execute(CommandExecuteUtil.getElement(getTarget()));
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
