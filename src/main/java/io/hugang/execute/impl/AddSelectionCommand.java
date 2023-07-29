package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.By;

@WebCommand
public class AddSelectionCommand extends Command {

    public static final String KEY_TARGET = "target";
    public static final String KEY_OPTION = "option";

    public AddSelectionCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {

        try {
            String target = getDictStr(KEY_TARGET, this.getTarget());
            String option = getDictStr(KEY_OPTION, this.getValue());

            CommandExecuteUtil.getElement(target).findElement(By.xpath("//option[. = '" + render(option) + "']")).click();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
