package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertNotEditableCommand extends Command {
    public AssertNotEditableCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(getTarget());
        // check if the element is not editable
        return !element.isEnabled();
    }
}
