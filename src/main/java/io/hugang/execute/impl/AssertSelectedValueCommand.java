package io.hugang.execute.impl;

import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AssertSelectedValueCommand extends Command {
    public AssertSelectedValueCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        WebElement element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[@value = '" + render(getValue()) + "']"));
        // return the selected status of the element
        return element.isSelected();
    }
}
