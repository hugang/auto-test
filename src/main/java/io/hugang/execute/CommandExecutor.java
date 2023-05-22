package io.hugang.execute;

import io.hugang.bean.Command;

/**
 * command executor interface
 * <p>
 *
 * @author hugang
 */
public interface CommandExecutor {
    /**
     * get command name
     *
     * @return command name
     */
    String getCommandName();

    /**
     * execute command
     *
     * @param command command
     * @return success or not
     */
    boolean execute(Command command);

    default boolean _execute(Command command) {
        if (CommandExecuteUtil.getTimes() > 1 && !command.getCommand().equals("end")) {
            CommandExecuteUtil.addToTimesCommands(command);
            return true;
        }else {
            return execute(command);
        }
    }
}
