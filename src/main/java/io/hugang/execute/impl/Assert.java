package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * Check that a variable is an expected value.
 * The variable's value will be converted to a string for comparison.
 * The test will stop if the assert fails.
 * <p>
 *
 * @author hugang
 */
public class Assert implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "assert";
    }

    @Override
    public boolean execute(Command command) {
        return CommandExecuteUtil.getVariable(command.getTarget()).equals(command.getValue());
    }
}
