package io.hugang.execute.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

import java.util.Map;

/**
 * set property command executor
 * <p>
 *
 * @author hugang
 */
public class SetProperty implements CommandExecutor {
    private static final Log log = LogFactory.get();

    @Override
    public String getCommandName() {
        return "setProperty";
    }

    /**
     * set property to variable maps
     *
     * @param command command
     * @return execute result
     */
    @Override
    public boolean execute(Command command) {
        if ("json".equals(command.getTarget())) {
            for (Map.Entry<String, Object> propertiesMap : JSONUtil.parseObj(command.getValue())) {
                CommandExecuteUtil.setVariable(propertiesMap.getKey(), propertiesMap.getValue().toString());
            }
        } else {
            log.error("unknown command target: {}", command.getTarget());
            return false;
        }
        return true;
    }
}
