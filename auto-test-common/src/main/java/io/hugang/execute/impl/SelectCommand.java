package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * select command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SelectCommand extends Command {
    public SelectCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute select command
     * <p> target：selector or id, value：option value
     *
     * @return success or not
     */
    @Override
    public boolean execute() {
        CommandExecuteUtil.getElement(this.getTarget()).selectOption(CommandExecuteUtil.render(this.getValue()));
        return true;
    }
}
