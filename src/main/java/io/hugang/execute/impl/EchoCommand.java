package io.hugang.execute.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class EchoCommand extends Command {
    private static final Log log = LogFactory.get();

    public EchoCommand() {
    }

    public EchoCommand(String command, String target) {
        super(command, target);
    }

    public EchoCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public EchoCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        log.info(CommandExecuteUtil.render(this.getTarget()));
        return true;
    }
}
