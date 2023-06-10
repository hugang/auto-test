package io.hugang.execute.impl;

import io.hugang.bean.Command;

import static com.codeborne.selenide.Selenide.open;

public class OpenCommand extends Command {
    public OpenCommand() {
    }

    public OpenCommand(String command, String target) {
        super(command, target);
    }

    public OpenCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public OpenCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        open(this.getTarget());
        return true;
    }
}
