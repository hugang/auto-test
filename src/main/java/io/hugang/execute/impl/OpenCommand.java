package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

import static com.codeborne.selenide.Selenide.open;

@WebCommand
public class OpenCommand extends Command {

    public static final String KEY_URL = "url";

    public OpenCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            String url = this.getDictStr(KEY_URL);
            if (url == null) {
                url = this.getTarget();
            }
            open(render(url));
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
