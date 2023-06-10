package io.hugang.execute.impl;

import com.codeborne.selenide.Selenide;
import io.hugang.bean.Command;

public class PauseCommand extends Command {
    @Override
    public boolean execute() {
        Selenide.sleep(Integer.parseInt(this.getTarget()));
        return true;
    }
}
