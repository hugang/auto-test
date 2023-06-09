package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, String> map = new HashMap<>();
        for (String key : this.getValue().split(",")) {
            if (CommandExecuteUtil.hasVariable(key)) {
                map.put(key, CommandExecuteUtil.getVariable(key));
            }
        }
        File file = FileUtil.writeString(JSONUtil.toJsonPrettyStr(map), this.getTarget(), Charset.defaultCharset());
        return file.exists();
    }
}
