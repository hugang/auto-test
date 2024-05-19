package io.hugang.execute.impl;

import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyNotEditableCommand extends Command {
    private static final Log log = Log.get();

    public VerifyNotEditableCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(getTarget());
        // check if the element is editable
        boolean enabled = element.isEnabled();
        log.info("verify target: {}, not editable={}", getTarget(), !enabled);
        return enabled;
    }
}
