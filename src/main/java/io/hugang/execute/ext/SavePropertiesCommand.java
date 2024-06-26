package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

import java.io.File;
import java.nio.charset.Charset;

/**
 * save properties command executor
 * <p>
 *
 * @author hugang
 */
public class SavePropertiesCommand extends Command {
    public SavePropertiesCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "saveProperties";
    }

    /**
     * execute save properties command
     *
     * @return success or not
     */
    @Override
    public boolean _execute() {

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
        this.appendDict("saveFilePath", saveFilePath);
        File file = FileUtil.writeString(JSONUtil.toJsonPrettyStr(map), saveFilePath, Charset.defaultCharset());
        return file.exists();
    }
}
