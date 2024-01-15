package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertElementPresentCommand extends Command {
    public AssertElementPresentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        return ObjectUtil.isNotEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
