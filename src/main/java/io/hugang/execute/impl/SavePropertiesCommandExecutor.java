package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

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
public class SavePropertiesCommandExecutor implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "saveProperties";
    }

    /**
     * execute save properties command
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {

        Map<String, String> map = new HashMap<>();
        for (String key : command.getValue().split(",")) {
            if (CommandExecuteUtil.hasVariable(key)) {
                map.put(key, CommandExecuteUtil.getVariable(key));
            }
        }
        File file = FileUtil.writeString(JSONUtil.toJsonPrettyStr(map), command.getTarget(), Charset.defaultCharset());
        return file.exists();
    }
}
