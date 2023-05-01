package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * wait for text command executor
 * <p>
 *
 * @author hugang
 */
public class WaitForTextCommandExecutor implements CommandExecutor {

    /**
     * execute wait for text command
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        int timeout = 0;
        try {
            do {
                SelenideElement $ = CommandExecuteUtil.getElement(command.getTarget());
                if (existValue(command, $.innerText())) {
                    return true;
                }
                if (existValue(command, $.innerHtml())) {
                    return true;
                }
                if (existValue(command, $.getText())) {
                    return true;
                }
                if (existValue(command, $.getValue())) {
                    return true;
                }
                timeout += 1000;
                // wait for 1 hour
                if (timeout > 60 * 60 * 1000) {
                    return false;
                }
                Thread.sleep(1000);
            } while (true);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * exist value
     *
     * @param command command
     * @param value   value
     * @return exist or not
     */
    private static boolean existValue(Command command, String value) {
        return null != value && value.contains(command.getValue());
    }
}
