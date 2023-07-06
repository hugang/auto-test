package io.hugang.execute.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import java.util.Map;

public class SetPropertyCommand extends Command {
    private static final Log log = LogFactory.get();

    public SetPropertyCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        if ("json".equals(this.getTarget())) {
            for (Map.Entry<String, Object> propertiesMap : JSONUtil.parseObj(this.getValue())) {
                CommandExecuteUtil.setVariable(propertiesMap.getKey(), propertiesMap.getValue().toString());
            }
        } else {
            log.error("unknown command target: {}", this.getTarget());
            return false;
        }
        return true;
    }
}
