package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AssertNotSelectedValueCommand extends Command {
    public AssertNotSelectedValueCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        // get the element by target
        WebElement element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[@value = '" + render(getValue()) + "']"));
        return !element.isSelected();
    }
}
