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
     * execute command
     *
     * @param command command
     * @return success or not
     */
    boolean execute(Command command);
}
