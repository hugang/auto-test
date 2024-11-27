package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.screenshot;

@WebCommand
public class ClickCommand extends Command {
    public ClickCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        execute(CommandExecuteUtil.getElement(render(getTarget())));
        generateReportData();
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }

    private void generateReportData() {
        String reportImageName = UUID.randomUUID().toString();
        screenshot(this.getReportPath().concat("/").concat(reportImageName));
        this.appendReport(RESULT_TYPE_IMG, "./".concat(reportImageName).concat(".png"));
    }
}
