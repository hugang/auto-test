package io.hugang.execute.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

/**
 * calc command
 */
public class CalcGroovyCommand extends Command {
    private static final Log log = Log.get();

    public CalcGroovyCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "calc.groovy";
    }

    @Override
    public boolean _execute() {
        try{
            // javascript expression to calculate
            String calcExpression = getTarget();
            // replace $$name with ThreadContext.get("name")
            calcExpression = calcExpression.replaceAll("\\$\\$([A-Za-z0-9_]+)", "io.hugang.util.ThreadContext.get(\"$1\")");
            calcExpression = "import io.hugang.util.ThreadContext\n" + calcExpression;

            // result variable name
            Binding binding = new Binding();
            // put variables into context
            for (String key : this.getVariables().keySet()) {
                binding.setVariable(key, this.getVariables().get(key));
            }
            GroovyShell shell = new GroovyShell(binding);
            Object result = shell.evaluate(calcExpression);
            if (StrUtil.isNotEmpty(getValue())) {
                String variableName = render(getValue());
                this.setVariable(variableName, result);
            }
        } catch (Exception e) {
            log.error("calc.groovy command error", e);
            return false;
        }
        return true;
    }
}
