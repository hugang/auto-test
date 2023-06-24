package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * Check that a variable is an expected value.
 * The variable's value will be converted to a string for comparison.
 * The test will stop if the assert fails.
 * <p>
 *
 * @author hugang
 */
public class AssertCommand extends Command {
    public AssertCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        return CommandExecuteUtil.getVariable(this.getTarget()).equals(this.getValue());
    }
}
