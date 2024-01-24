package io.hugang.execute.impl;

import cn.hutool.log.Log;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class VerifyTitleCommand extends Command {
    private static final Log log = Log.get();

    public VerifyTitleCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        boolean equals = WebDriverRunner.getWebDriver().getTitle().equals(render(this.getTarget()));
        log.info("verify target: {}, equal={}", getTarget(), equals);
        return true;
    }
}
