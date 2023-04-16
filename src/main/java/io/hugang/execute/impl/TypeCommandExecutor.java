package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;

import static com.codeborne.selenide.Selenide.$;

/**
 * type command executor
 * <p>
 *
 * @author hugang
 */
public class TypeCommandExecutor implements CommandExecutor {
    /**
     * type command executor
     * <p> target：selector or id, value：input value
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        String[] split = command.getTarget().split("=");
        if (split.length == 2) {
            if (split[0].equals("selector")) {
                $(split[1]).setValue(command.getValue());
                return true;
            } else if (split[0].equals("id")) {
                $("#" + split[1]).setValue(command.getValue());
                return true;
            } else {
                $("input[" + split[0] + "=" + split[1] + "]").setValue(command.getValue());
                return true;
            }
        }
        return false;
    }
}
