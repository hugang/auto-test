package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;

public class PauseCommand extends Command {
    public PauseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        if (ObjectUtil.isEmpty(this.getValue())) {
            return true;
        }
        try {
            Thread.sleep(Integer.parseInt(this.getValue()));
        } catch (InterruptedException e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
