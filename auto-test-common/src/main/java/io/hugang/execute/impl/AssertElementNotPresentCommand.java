package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertElementNotPresentCommand extends Command {
    public AssertElementNotPresentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        return ObjectUtil.isEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
