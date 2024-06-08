package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

/**
 * write to file command
 */
public class WriteToFieCommand extends Command {
    private static final Log log = Log.get();

    public WriteToFieCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "writeToFile";
    }

    @Override
    public boolean _execute() {
        // target file path
        String targetFile = getFilePath(render(getTarget()));
        // content to write
        String variableName = render(getValue());
        // type, append or write
        String type = this.getDictStr("type");
        // default type is append
        if (StrUtil.isEmpty(type)) {
            type = "append";
        }
        if ((!"append".equals(type) && !"write".equals(type))) {
            log.error("type error");
            return false;
        }
        if ("write".equals(type)) {
            // remove file if exists
            FileUtil.del(targetFile);
        }
        try {
            // write content to file
            FileUtil.appendString(variableName, targetFile, "UTF-8");
        } catch (Exception e) {
            log.error("write to file error", e);
            return false;
        }
        return true;
    }
}
