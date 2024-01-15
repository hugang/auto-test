package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertEditableCommand extends Command {

    public AssertEditableCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            SelenideElement element = CommandExecuteUtil.getElement(getTarget());
            return element.isEnabled() && element.getAttribute("readonly") == null;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
