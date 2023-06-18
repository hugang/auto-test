package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class AssertElementNotPresentCommand extends Command {
    public AssertElementNotPresentCommand() {
    }

    public AssertElementNotPresentCommand(String command, String target) {
        super(command, target);
    }

    public AssertElementNotPresentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertElementNotPresentCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        return ObjectUtil.isEmpty(CommandExecuteUtil.findElements(this.getTarget()));
    }
}
