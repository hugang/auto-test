package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class EchoCommand extends Command {
    public EchoCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    private static final Log log = Log.get();
    @Override
    public boolean _execute() {
        String target = this.getTarget();
        if (target == null) {
            log.error("echo target is null");
            return false;
        }
        log.info(render(target));
        return true;
    }
}
