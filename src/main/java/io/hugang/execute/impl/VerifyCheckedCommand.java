package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyCheckedCommand extends Command {
    private static final Log log = Log.get();

    public VerifyCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        try {
            String target = getTarget();
            boolean selected = CommandExecuteUtil.getElement(target).isSelected();
            log.info("verify target: {}, equal={}", target, selected);
        } catch (Exception e) {
            // throw new CommandExecuteException(e);
        }
        return true;
    }
}
