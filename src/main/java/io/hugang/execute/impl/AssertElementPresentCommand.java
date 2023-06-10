package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertElementPresentCommand extends Command {
    public AssertElementPresentCommand() {
    }

    public AssertElementPresentCommand(String command, String target) {
        super(command, target);
    }

    public AssertElementPresentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertElementPresentCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        return ObjectUtil.isNotEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
