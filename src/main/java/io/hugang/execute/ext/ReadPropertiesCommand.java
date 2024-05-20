package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;

/**
 * set property command executor
 * <p>
 *
 * @author hugang
 */
public class ReadPropertiesCommand extends Command {

    public static final String KEY_FILE = "value";
    public static final String VALUE_TYPE_JSON = "json";
    public static final String VALUE_TYPE_PROPERTIES = "properties";

    public ReadPropertiesCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "readProperties";
    }

    /**
     * read properties file and set to variable maps
     *
     * @return execute result
     */
    @Override
    public boolean _execute() {
        String type = this.getTarget();
        String file = this.getDictStr(KEY_FILE, this.getValue());
        String filePath = this.getFilePath(file);

        if (VALUE_TYPE_JSON.equals(type)) {
            JSONObject json = (JSONObject) JSONUtil.readJSON(FileUtil.file(filePath), CharsetUtil.CHARSET_UTF_8);
            json.forEach(this::setVariable);
        } else if (VALUE_TYPE_PROPERTIES.equals(type)) {
            // read a properties file
            Setting setting = SettingUtil.get(file);
            setting.forEach(this::setVariable);
        } else {
            throw new CommandExecuteException("not support type: " + type);
        }

        return true;
    }
}
