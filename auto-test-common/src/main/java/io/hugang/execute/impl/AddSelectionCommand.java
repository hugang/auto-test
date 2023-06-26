package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.By;

@WebCommand
public class AddSelectionCommand extends Command {

    public AddSelectionCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            CommandExecuteUtil.getElement(this.getTarget()).findElement(By.xpath("//option[. = '" + this.getValue() + "']")).click();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
