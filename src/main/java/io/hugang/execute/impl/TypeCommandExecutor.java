package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

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
        CommandExecuteUtil.getElement(command.getTarget()).setValue(command.getValue());
        return true;
    }
}
