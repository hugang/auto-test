package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import static com.codeborne.selenide.Selenide.open;

@WebCommand
public class OpenCommand extends Command {
    public OpenCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            open(CommandExecuteUtil.render(this.getTarget()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandExecuteException("OpenCommand execute failed");
        }
    }
}
