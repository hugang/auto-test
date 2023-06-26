package io.hugang.execute.impl;

import com.codeborne.selenide.Selenide;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;

@WebCommand
public class PauseCommand extends Command {
    public PauseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        Selenide.sleep(Integer.parseInt(this.getValue()));
        return true;
    }
}
