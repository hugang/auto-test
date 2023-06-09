package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;

import static com.codeborne.selenide.Selenide.open;

@WebCommand
public class OpenCommand extends Command {
    public OpenCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        open(this.getTarget());
        return true;
    }
}
