package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.ReportCommand;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
@ReportCommand("before") // 点击之前保存图片
public class ClickCommand extends Command {
    public ClickCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        execute(CommandExecuteUtil.getElement(render(getTarget())));
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
