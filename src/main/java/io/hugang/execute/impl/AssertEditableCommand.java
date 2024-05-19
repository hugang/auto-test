package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertEditableCommand extends Command {
    public AssertEditableCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            SelenideElement element = CommandExecuteUtil.getElement(getTarget());
            return element.isEnabled() && element.getAttribute("readonly") == null;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
