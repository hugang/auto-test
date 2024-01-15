package io.hugang.execute.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.execute.Command;

public class EchoCommand extends Command {
    private static final Log log = LogFactory.get();

    public EchoCommand(String command, String target, String value) {
        super(command, target, value);
    }

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
