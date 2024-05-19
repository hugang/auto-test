package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class VerifyNotCheckedCommand extends Command {
    private static final Log log = Log.get();

    public VerifyNotCheckedCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        boolean selected = CommandExecuteUtil.getElement(target).isSelected();
        log.info("verify target: {}, not checked={}", target, !selected);
        return true;
    }
}
