package io.hugang.execute.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.execute.Command;

/**
 * increase number command executor
 * <p>
 *
 * @author hugang
 */
public class IncreaseNumberCommand extends Command {
    private static final Log log = LogFactory.get();

    public IncreaseNumberCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute command to increase number
     *
     * @return execute result
     */
    @Override
    public boolean execute() {
        try {
            // get from variable map
            String variable = this.getVariableStr(this.getTarget());
            if (!StrUtil.isNumeric(variable)) {
                return false;
            }
            int originNumber = NumberUtil.parseInt(variable);
            String value = this.getDictStr("value");
            if (!StrUtil.isNumeric(value)) {
                return false;
            }
            int result = originNumber + NumberUtil.parseInt(value);

            // write back to variable map
            this.setVariable(this.getTarget(), result);
            return true;
        } catch (Exception e) {
            log.error("increase number command execute error", e);
            return false;
        }
    }
}
