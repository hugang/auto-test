package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * select command executor
 * <p>
 *
 * @author hugang
 */
public class SelectCommand extends Command {

    public SelectCommand() {
    }

    public SelectCommand(String command, String target) {
        super(command, target);
    }

    public SelectCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public SelectCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
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
