package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

public class AssertNotEditableCommand extends Command {
    public AssertNotEditableCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(getTarget());
        // check if the element is editable
        return element.isEnabled();
    }
}
