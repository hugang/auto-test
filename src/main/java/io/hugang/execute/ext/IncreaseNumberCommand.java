package io.hugang.execute.ext;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import io.hugang.execute.Command;

/**
 * increase number command executor
 * <p>
 *
 * @author hugang
 */
public class IncreaseNumberCommand extends Command {
    private static final Log log = Log.get();

    @Override
    public String getCommand() {
        return "increaseNumber";
    }

    /**
     * execute command to increase number
     *
     * @return execute result
     */
    @Override
    public boolean _execute() {
        try {
            // initialize result
            int result;
            int originNumber = 0;

            // get from variable map
            String variable = this.getVariableStr(this.getTarget());
            if (StrUtil.isEmpty(variable)) {
                log.error("increase number command target is empty");
                return false;
            }
            if (StrUtil.isNumeric(variable)) {
                originNumber = NumberUtil.parseInt(variable);
            }
            String value = this.getDictStr("value");
            if (!StrUtil.isNumeric(value)) {
                log.error("increase number command value is not a number");
                return false;
            }
            result = originNumber + NumberUtil.parseInt(value);

            // write back to variable map
            this.setVariable(this.getTarget(), result);
            return true;
        } catch (Exception e) {
            log.error("increase number command execute error", e);
            return false;
        }
    }
}
