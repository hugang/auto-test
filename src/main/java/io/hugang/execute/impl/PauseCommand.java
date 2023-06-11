package io.hugang.execute.impl;

import com.codeborne.selenide.Selenide;
import io.hugang.bean.Command;

public class PauseCommand extends Command {
    public PauseCommand() {
    }

    public PauseCommand(String command, String target) {
        super(command, target);
    }

    public PauseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public PauseCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        Selenide.sleep(Integer.parseInt(this.getTarget()));
        return true;
    }
}
