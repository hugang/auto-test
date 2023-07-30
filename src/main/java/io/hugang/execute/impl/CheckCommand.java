package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

@WebCommand
public class CheckCommand extends Command {

    public CheckCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        // Check a toggle-button (checkbox/radio).
        execute(CommandExecuteUtil.getElement(getTarget()));
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled() && !$.isSelected()) {
            $.click();
        }
    }
}
