package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import java.io.File;

/**
 * set property command executor
 * <p>
 *
 * @author hugang
 */
public class ReadPropertiesCommand extends Command {
    public ReadPropertiesCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * read properties file and set to variable maps
     *
     * @return execute result
     */
    @Override
    public boolean execute() {

        if ("json".equals(this.getTarget())) {
            File file = FileUtil.file(this.getValue());

            if (!file.exists()){
                file = FileUtil.file(SystemUtil.getUserInfo().getCurrentDir()+File.separator+this.getValue());
            }
            if (!file.exists()){
                return false;
            }
            JSONObject json = (JSONObject) JSONUtil.readJSON(file, CharsetUtil.CHARSET_UTF_8);
            json.forEach((key, value) -> CommandExecuteUtil.setVariable(key, value.toString()));
        }

        return true;
    }
}
