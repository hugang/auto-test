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

    public AssertCommand() {
    }

    public AssertCommand(String command, String target) {
        super(command, target);
    }

    public AssertCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public AssertCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        return CommandExecuteUtil.getVariable(this.getTarget()).equals(this.getValue());
    }
}
