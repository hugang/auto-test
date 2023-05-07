package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * set element to property command executor
 * <p>
 *
 * @author hugang
 */
public class SetElementToPropertyCommandExecutor implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "setElementToProperty";
    }

    /**
     * set element to property
     *
     * @param command command
     * @return execute result
     */
    @Override
    public boolean execute(Command command) {
        SelenideElement $ = CommandExecuteUtil.getElement(command.getTarget());
        // set $ to variable maps
        CommandExecuteUtil.setVariable(command.getValue(), $);
        return true;
    }
}
