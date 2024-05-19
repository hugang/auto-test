package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

/**
 * select command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SelectCommand extends Command {
    public SelectCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    /**
     * execute select command
     * <p> target：selector or id, value：option value
     *
     * @return success or not
     */
    @Override
    public boolean _execute() {
        CommandExecuteUtil.getElement(this.getTarget()).selectOption(render(this.getDictStr("value", this.getValue())));
        return true;
    }
}
