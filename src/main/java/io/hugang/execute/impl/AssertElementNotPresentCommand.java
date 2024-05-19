package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertElementNotPresentCommand extends Command {
    public AssertElementNotPresentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        return ObjectUtil.isEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
