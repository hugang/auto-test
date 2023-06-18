package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class ClickCommand extends Command {

    public ClickCommand() {
    }

    public ClickCommand(String command, String target) {
        super(command, target);
    }

    public ClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public ClickCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        execute(CommandExecuteUtil.getElement(this.getTarget()));
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
