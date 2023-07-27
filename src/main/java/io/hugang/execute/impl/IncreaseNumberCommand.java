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
        int i = NumberUtil.parseInt(variable);
        String[] split = this.getValue().split(",");
        int unit = NumberUtil.parseInt(split[0]);
        if (split.length > 1) {
            int length = NumberUtil.parseInt(split[1]);
            String padStr = split.length > 2 ? split[2] : "0";
            result = StrUtil.padPre(StrUtil.toString(i + unit), length, padStr);
        } else {
            result = StrUtil.toString(i + unit);
        }
        // write back to variable map
        CommandExecuteUtil.setVariable(this.getTarget(), result);
        return true;
    }
}
