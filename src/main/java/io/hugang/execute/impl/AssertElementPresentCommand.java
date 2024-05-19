package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertElementPresentCommand extends Command {
    public AssertElementPresentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        return ObjectUtil.isNotEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
