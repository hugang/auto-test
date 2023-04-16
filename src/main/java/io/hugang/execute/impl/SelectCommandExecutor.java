package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;

import static com.codeborne.selenide.Selenide.$;

/**
 * select command executor
 * <p>
 *
 * @author hugang
 */
public class SelectCommandExecutor implements CommandExecutor {
    /**
     * execute select command
     * <p> target：selector or id, value：option value
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        String[] split = command.getTarget().split("=");
        if (split.length == 2) {
            if (split[0].equals("selector")) {
                $(split[1]).selectOption(command.getValue());
            } else if (split[0].equals("id")) {
                $("#" + split[1]).selectOption(command.getValue());
            }
            return true;
        }
        return false;
    }
}
