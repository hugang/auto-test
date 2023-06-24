package io.hugang.execute.impl;

import cn.hutool.core.util.StrUtil;
import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class ClickCommand extends Command {

    public ClickCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        if (StrUtil.isEmpty(this.getValue())) {
            return false;
        }
        execute(CommandExecuteUtil.getElement(this.getTarget()));
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
