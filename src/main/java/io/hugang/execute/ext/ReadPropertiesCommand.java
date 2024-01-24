package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

    public ReadPropertiesCommand(String command, String target, String value) {
        super(command, target, value);
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

        if (VALUE_TYPE_JSON.equals(type)) {
            String filePath = this.getFilePath(file);
            JSONObject json = (JSONObject) JSONUtil.readJSON(FileUtil.file(filePath), CharsetUtil.CHARSET_UTF_8);
            json.forEach(this::setVariable);
        }

        return true;
    }
}
