package io.hugang.execute.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.execute.Command;

import java.util.Map;

public class SetPropertyCommand extends Command {
    private static final Log log = Log.get();
    public static final String KEY_TYPE = "type";
    public static final String VALUE_TYPE_JSON = "json";

    public SetPropertyCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        String type = this.getDictStr(KEY_TYPE, this.getTarget());
        if (VALUE_TYPE_JSON.equals(type)) {
            for (Map.Entry<String, Object> propertiesMap : JSONUtil.parseObj(this.render(this.getDictStr("value", this.getValue())))) {
                this.setVariable(propertiesMap.getKey(), propertiesMap.getValue());
            }
        } else {
            log.error("unknown command target: {}", this.getTarget());
            return false;
        }
        return true;
    }
}
