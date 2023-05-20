package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class AssertElementPresent implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "assertElementPresent";
    }

    @Override
    public boolean execute(Command command) {
        return ObjectUtil.isNotEmpty(CommandExecuteUtil.findElements(command.getTarget()));
    }
}
