package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
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

        if (ObjectUtil.isEmpty(this.getValue())) {
            map = CommandExecuteUtil.getVariables();
        } else {
            for (String key : this.getValue().split(",")) {
                if (CommandExecuteUtil.hasVariable(key)) {
                    map.put(key, CommandExecuteUtil.getVariable(key));
                }
            }
        }

        String saveFilePath = CommandExecuteUtil.getFilePath(CommandExecuteUtil.render(this.getTarget()));
        File file = FileUtil.writeString(JSONUtil.toJsonPrettyStr(map), saveFilePath, Charset.defaultCharset());
        return file.exists();
    }
}
