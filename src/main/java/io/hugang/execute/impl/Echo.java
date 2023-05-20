package io.hugang.execute.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class Echo implements CommandExecutor {
    private static final Log log = LogFactory.get();

    @Override
    public String getCommandName() {
        return "echo";
    }

    @Override
    public boolean execute(Command command) {
        log.info(CommandExecuteUtil.render(command.getTarget()));
        return true;
    }
}
