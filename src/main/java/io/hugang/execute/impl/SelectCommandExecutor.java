package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * select command executor
 * <p>
 *
 * @author hugang
 */
public class SelectCommandExecutor implements CommandExecutor {
    /**
     * execute select command
     * <p> target：selector or id, value：option value
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        SelenideElement $ = CommandExecuteUtil.getElement(command.getTarget());
        if ($ == null) {
            return false;
        }
        $.selectOption(command.getValue());
        return true;
    }
}
