package io.hugang.execute.impl;

import io.hugang.execute.CommandExecutor;
import io.hugang.bean.Command;

import static com.codeborne.selenide.Selenide.open;

/**
 * open url command executor
 * <p>
 *
 * @author hugang
 */
public class OpenCommandExecutor implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "open";
    }

    /**
     * execute open command
     * <p> target：url
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        open(command.getTarget());
        return true;
    }
}
