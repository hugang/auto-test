package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;

import static com.codeborne.selenide.Selenide.open;

@WebCommand
public class OpenCommand extends Command {
    public static final String KEY_URL = "url";

    public OpenCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            String url = this.getDictStr(KEY_URL);
            if (url == null) {
                url = this.getTarget();
            }
            String realUrl = render(url);
            open(realUrl);
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
