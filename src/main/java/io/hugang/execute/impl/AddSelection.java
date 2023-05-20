package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.By;

public class AddSelection implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "addSelection";
    }

    @Override
    public boolean execute(Command command) {
        CommandExecuteUtil.getElement(command.getTarget()).findElement(By.xpath("//option[. = '" + command.getValue() + "']")).click();
        return true;
    }
}
