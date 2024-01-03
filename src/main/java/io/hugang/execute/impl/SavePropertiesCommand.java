package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.hugang.bean.Command;

import java.io.File;
import java.nio.charset.Charset;

/**
 * save properties command executor
 * <p>
 *
 * @author hugang
 */
public class SavePropertiesCommand extends Command {
    public SavePropertiesCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute save properties command
     *
     * @return success or not
     */
    @Override
    public boolean execute() {

        Dict map;

        if (ObjectUtil.isEmpty(this.getValue()) || JSONUtil.isTypeJSONObject(this.getValue())) {
            map = this.getVariables();
        } else {
            map = Dict.create();
            for (String key : this.getValue().split(",")) {
                if (this.hasVariable(key)) {
                    map.put(key, this.getVariable(key));
                }
            }
        }

        String saveFilePath = this.getFilePath(render(this.getTarget()), true);
        File file = FileUtil.writeString(JSONUtil.toJsonPrettyStr(map), saveFilePath, Charset.defaultCharset());
        return file.exists();
    }
}
