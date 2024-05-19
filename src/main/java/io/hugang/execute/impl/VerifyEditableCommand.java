package io.hugang.execute.impl;

import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyEditableCommand extends Command {
    private static final Log log = Log.get();

    public VerifyEditableCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            SelenideElement element = CommandExecuteUtil.getElement(getTarget());
            boolean readonly = element.isEnabled() && element.getAttribute("readonly") == null;
            log.info("verify target: {}, equal={}", getTarget(), readonly);
        } catch (Exception e) {
            // throw new CommandExecuteException(e);
        }
        return true;
    }
}
