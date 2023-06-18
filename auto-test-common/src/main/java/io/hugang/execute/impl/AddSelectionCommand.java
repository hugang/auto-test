package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.By;

public class AddSelectionCommand extends Command {
    public AddSelectionCommand() {
    }

    public AddSelectionCommand(String command, String target) {
        super(command, target);
    }

    public AddSelectionCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AddSelectionCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
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