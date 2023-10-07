package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@WebCommand
public class RemoveSelectionCommand extends Command {

    public static final String KEY_VALUE = "value";

    public RemoveSelectionCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {

        try {
            String target = this.getTarget();
            String option = getDictStr(KEY_VALUE, this.getValue());

            WebElement element = CommandExecuteUtil.getElement(target).findElement(By.xpath("//option[. = '" + render(option) + "']"));
            if (element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
