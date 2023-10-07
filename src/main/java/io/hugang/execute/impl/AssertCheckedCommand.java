package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class AssertCheckedCommand extends Command {

    public AssertCheckedCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            String target = getTarget();
            return CommandExecuteUtil.getElement(target).isSelected();
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
