package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

/**
 * Check that a variable is an expected value.
 * The variable's value will be converted to a string for comparison.
 * The test will continue even if the verify fails.
 * <p>
 *
 * @author hugang
 */
public class VerifyCommand extends Command {
    private static final Log log = Log.get();

    public VerifyCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        String value = this.getDictStr("value", this.getValue());
        log.info("verify target: {}, value: {}, equal={}", target, value, this.getVariable(target).equals(value));
        return true;
    }
}
