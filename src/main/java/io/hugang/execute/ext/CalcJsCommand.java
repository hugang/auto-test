package io.hugang.execute.ext;

import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.JavaScriptEvaluator;

/**
 * calc command
 */
public class CalcJsCommand extends Command {
    private static final Log log = Log.get();

    public CalcJsCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "calc.js";
    }

    @Override
    public boolean _execute() {
        // javascript expression to calculate
        String calcExpression = getTarget();
        // result variable name
        String variableName = render(getValue());

        // execute javascript expression
        try {
            Object result = JavaScriptEvaluator.evaluate(calcExpression, this.getVariables());
            this.setVariable(variableName, result);
        } catch (Exception e) {
            log.error("calc error", e);
            return false;
        }

        return true;
    }
}
