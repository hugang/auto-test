package io.hugang.execute.impl;

import com.codeborne.selenide.Selenide;
import io.hugang.bean.Command;

/**
 * sleep command executor
 * <p>
 *
 * @author hugang
 */
public class SleepCommandExecutor implements io.hugang.execute.CommandExecutor {
    /**
     * sleep command executor
     * <p> target：sleep time, value：null
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        Selenide.sleep(Integer.parseInt(command.getValue()));
        return true;
    }
}
