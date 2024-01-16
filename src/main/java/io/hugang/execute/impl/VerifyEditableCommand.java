package io.hugang.execute.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyEditableCommand extends Command {
    private static final Log log = LogFactory.get();

    public VerifyEditableCommand(String command, String target, String value) {
        super(command, target, value);
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
