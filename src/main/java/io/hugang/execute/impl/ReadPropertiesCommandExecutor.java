package io.hugang.execute.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

import java.io.File;

/**
 * set property command executor
 * <p>
 *
 * @author hugang
 */
public class ReadPropertiesCommandExecutor implements CommandExecutor {
    /**
     * read properties file and set to variable maps
     *
     * @param command command
     * @return execute result
     */
    @Override
    public boolean execute(Command command) {

        if ("json".equals(command.getTarget())) {
            JSONObject json = (JSONObject) JSONUtil.readJSON(new File(command.getValue()), CharsetUtil.CHARSET_UTF_8);
            json.forEach((key, value) -> CommandExecuteUtil.setVariable(key, value.toString()));
        }

        return true;
    }
}
