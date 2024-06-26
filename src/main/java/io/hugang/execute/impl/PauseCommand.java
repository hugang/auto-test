package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.Selenide;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class PauseCommand extends Command {
    public PauseCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        if (ObjectUtil.isEmpty(this.getTarget())) {
            return true;
        }
        Selenide.sleep(Long.parseLong(this.getTarget()));
        return true;
    }
}
