package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyElementNotPresentCommand extends Command {
    private static final Log log = Log.get();

    public VerifyElementNotPresentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        boolean empty = ObjectUtil.isEmpty(CommandExecuteUtil.findElements(this.getTarget()));
        log.info("verify target: {}, equal={}", getTarget(), empty);
        return true;
    }
}
