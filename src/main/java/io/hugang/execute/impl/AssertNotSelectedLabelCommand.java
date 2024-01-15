package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AssertNotSelectedLabelCommand extends Command {
    public AssertNotSelectedLabelCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        // get the element by target
        WebElement element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[@label = '" + render(getValue()) + "']"));
        // if element is null, get the element by title
        if (element == null) {
            element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[. = '" + render(getValue()) + "']"));
        }
        return !element.isSelected();
    }
}
