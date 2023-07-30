package io.hugang.execute.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import java.util.Map;

public class SetPropertyCommand extends Command {
    private static final Log log = LogFactory.get();
    public static final String KEY_TYPE = "type";
    public static final String VALUE_TYPE_JSON = "json";

    public SetPropertyCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String type = this.getDictStr(KEY_TYPE, this.getTarget());
        if (VALUE_TYPE_JSON.equals(type)) {
            for (Map.Entry<String, Object> propertiesMap : JSONUtil.parseObj(CommandExecuteUtil.render(this.getDictStr("value", this.getValue())))) {
                CommandExecuteUtil.setVariable(propertiesMap.getKey(), propertiesMap.getValue());
            }
        } else {
            log.error("unknown command target: {}", this.getTarget());
            return false;
        }
        return true;
    }
}
