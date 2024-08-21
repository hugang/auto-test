package io.hugang.execute.ext;

import cn.hutool.log.Log;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.config.QLExpressRunStrategy;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

/**
 * calc command
 */
public class CalcCommand extends Command {
    private static final Log log = Log.get();

    public CalcCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "calc";
    }

    @Override
    public boolean _execute() {
        // create express runner, set precise to true
        ExpressRunner runner = new ExpressRunner(true, false);
        // forbid invoke security risk constructors and methods
        QLExpressRunStrategy.setForbidInvokeSecurityRiskConstructors(true);
        QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);

        DefaultContext<String, Object> context = new DefaultContext<>();

        // put variables into context
        for (String key : this.getVariables().keySet()) {
            context.put(key, this.getVariables().get(key));
        }

        // javascript expression to calculate
        String calcExpression = getTarget();
        // result variable name
        String variableName = render(getValue());

        // execute javascript expression
        try {
            // set timeout to 1000ms to avoid infinite loop
            Object result = runner.execute(calcExpression, context, null, true, false, 1000);
            this.setVariable(variableName, result);
        } catch (Exception e) {
            log.error("calc error", e);
            return false;
        }

        return true;
    }
}
