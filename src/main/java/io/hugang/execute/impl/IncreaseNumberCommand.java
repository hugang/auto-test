package io.hugang.execute.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * increase number command executor
 * <p>
 *
 * @author hugang
 */
public class IncreaseNumberCommand extends Command {
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
        // get from variable map
        String variable = CommandExecuteUtil.getVariableStr(this.getTarget());
        if (!StrUtil.isNumeric(variable)) {
            return false;
        }
        String result;
        int originNumber = NumberUtil.parseInt(variable);
        String step = this.getDictStr("step");
        String padSize = this.getDictStr("padSize");
        String padStr = this.getDictStr("padStr", "0");

        if (StrUtil.isEmpty(step)){
            String[] split = this.getValue().split(",");
            step = split[0];
            if (split.length > 1) {
                padSize = split[1];
            }
            if (split.length > 2) {
                padStr = split[2];
            }
        }

        if (StrUtil.isNotEmpty(padSize)){
            result = StrUtil.padPre(StrUtil.toString(originNumber + NumberUtil.parseInt(step)), NumberUtil.parseInt(padSize), padStr);
        } else {
            result = StrUtil.toString(originNumber + NumberUtil.parseInt(step));
        }

        // write back to variable map
        CommandExecuteUtil.setVariable(this.getTarget(), result);
        return true;
    }
}
