package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@WebCommand
public class AssertNotSelectedLabelCommand extends Command {
    public AssertNotSelectedLabelCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        WebElement element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[@label = '" + render(getValue()) + "']"));
        // if element is null, get the element by title
        if (element == null) {
            element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[. = '" + render(getValue()) + "']"));
        }
        return !element.isSelected();
    }
}
