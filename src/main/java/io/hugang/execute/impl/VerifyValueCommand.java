package io.hugang.execute.impl;

import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyValueCommand extends Command {
    private static final Log log = Log.get();

    public VerifyValueCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(render(getTarget()));
        // Get the text of the element
        String elementText = element.getValue();
        // Compare the text of the element with the value
        boolean b = elementText != null && elementText.equals(render(getValue()));
        log.info("verify target: {}, value={}, equal={}", getTarget(), elementText, b);
        return true;
    }
}
