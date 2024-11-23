package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.screenshot;

@WebCommand
public class AddSelectionCommand extends Command {

    public static final String KEY_VALUE = "value";

    public AddSelectionCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {

        try {
            String target = render(this.getTarget());
            String option = getDictStr(KEY_VALUE, this.getValue());

            WebElement element = CommandExecuteUtil.getElement(target).findElement(By.xpath("//option[. = '" + render(option) + "']"));
            if (!element.isSelected()) {
                element.click();
            }
            generateReportData();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }

    private void generateReportData(){
        this.appendReport(RESULT_TYPE_MSG, render(this.getTarget()).concat("  |  ").concat(getDictStr(KEY_VALUE, this.getValue())));
        String reportImageName = UUID.randomUUID().toString();
        screenshot(this.getReportPath().concat("/").concat(reportImageName));
        this.appendReport(RESULT_TYPE_IMG, "./".concat(reportImageName).concat(".png"));
    }
}
