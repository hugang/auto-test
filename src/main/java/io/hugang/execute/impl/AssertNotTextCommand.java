package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

public class AssertNotTextCommand extends Command {
    public AssertNotTextCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(render(getTarget()));
        // Get the text of the element
        String elementText = element.getText();
        // Compare the text of the element with the value
        return !elementText.equals(render(getValue()));
    }
}
