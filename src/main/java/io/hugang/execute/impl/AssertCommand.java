package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

/**
 * Check that a variable is an expected value.
 * The variable's value will be converted to a string for comparison.
 * The test will stop if the assert fails.
 * <p>
 *
 * @author hugang
 */
public class AssertCommand extends Command {
    public static final String VALUE = "value";

    public AssertCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        String value = this.getDictStr(VALUE, this.getValue());
        return this.getVariable(target).equals(value);
    }
}
