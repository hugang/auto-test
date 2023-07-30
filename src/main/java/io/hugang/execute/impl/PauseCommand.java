package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.Selenide;
import io.hugang.bean.Command;

public class PauseCommand extends Command {
    public PauseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        if (ObjectUtil.isEmpty(this.getTarget())) {
            return true;
        }
        Selenide.sleep(Long.parseLong(this.getTarget()));
        return true;
    }
}
