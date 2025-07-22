package io.hugang.execute.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * calc command
 */
public class CalcQlExpressCommand extends Command {
    private static final Log log = Log.get();

    public CalcQlExpressCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "calc.ql";
    }

    @Override
    public boolean _execute() {
        // create express runner, set precise to true
        Express4Runner runner = getExpress4Runner();
        Map<String, Object> context = new HashedMap<>();

        // put variables into context
        for (String key : this.getVariables().keySet()) {
            context.put(key, this.getVariables().get(key));
        }

        try {
            // javascript expression to calculate
            String calcExpression = getTarget();

            QLOptions qlOptions = QLOptions.builder().
                    precise(true) // enable precise mode
                    .timeoutMillis(1000L) // set timeout to 1000 milliseconds
                    .build();
            // execute javascript expression
            Object result = runner.execute(calcExpression, context, qlOptions).getResult();
            this.appendReport(RESULT_TYPE_MSG, result.toString());

            if (StrUtil.isNotEmpty(getValue())) {
                // result variable name
                String variableName = render(getValue());
                this.setVariable(variableName, result);
            }
        } catch (Exception e) {
            log.error("calc error", e);
            return false;
        }

        return true;
    }

    private static Express4Runner getExpress4Runner() {
        Express4Runner runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        // custom function : round
        runner.addVarArgsFunction("round", (args) -> {
            if (args.length == 2) {
                double value = Double.parseDouble(args[0].toString());
                int scale = Integer.parseInt(args[1].toString());
                return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
            }
            throw new IllegalArgumentException("Invalid arguments for round function");
        });
        return runner;
    }
}
