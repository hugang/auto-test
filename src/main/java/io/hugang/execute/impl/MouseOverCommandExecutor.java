package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * mouse over command executor
 * <p>
 * This class is used to execute the mouse over command.
 * It will use selenium to execute the command.
 * The target of the command is the element to be mouse overed.
 * The value of the command is not used.
 * The command will be executed successfully if the element is found.
 * <p>
 * 
 * @author hugang
 * 
 */
public class MouseOverCommandExecutor implements CommandExecutor {

    /**
     * get command name
     * 
     * @return command name
     */
    @Override
    public String getCommandName() {
        return "mouseOver";
    }

    /**
     * execute command
     * @param command command
     * @return true if the command is executed successfully
     */
    @Override
    public boolean execute(Command command) {
        // use selenium to execute the command
        SelenideElement $ = CommandExecuteUtil.getElement(command.getTarget());
        $.hover();
        return true;
    }

}
