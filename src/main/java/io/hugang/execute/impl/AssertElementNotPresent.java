package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class AssertElementNotPresent implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "assertElementNotPresent";
    }

    @Override
    public boolean execute(Command command) {
        return ObjectUtil.isEmpty(CommandExecuteUtil.findElements(command.getTarget()));
    }
}
