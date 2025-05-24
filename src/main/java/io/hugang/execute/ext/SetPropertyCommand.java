package io.hugang.execute.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

import java.util.Map;

public class SetPropertyCommand extends Command {
    private static final Log log = Log.get();
    public static final String KEY_TYPE = "type";
    public static final String VALUE_TYPE_JSON = "json";

    public SetPropertyCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "setProperty";
    }

    @Override
    public boolean _execute() {
        String type = this.getDictStr(KEY_TYPE, this.getTarget());
        if (VALUE_TYPE_JSON.equals(type)) {
            log.info("setProperty by json: {}", this.getDictStr("value", this.getValue()));
            for (Map.Entry<String, Object> propertiesMap : JSONUtil.parseObj(this.render(this.getDictStr("value", this.getValue())))) {
                this.setVariable(propertiesMap.getKey(), propertiesMap.getValue());
            }
        } else if (StrUtil.isNotEmpty(type)) {
            log.info("setProperty {}: {}", type, this.getDictStr("value", this.getValue()));
            this.setVariable(type, this.render(this.getDictStr("value", this.getValue())));
        }
        return true;
    }
}
