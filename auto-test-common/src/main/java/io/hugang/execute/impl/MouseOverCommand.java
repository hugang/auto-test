package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * mouse over command executor
 * <p>
 * This class is used to execute the mouse over command.
 * It will use selenium to execute the command.
 * The target of the command is the element to be moused overed.
 * The value of the command is not used.
 * The command will be executed successfully if the element is found.
 * <p>
 * 
 * @author hugang
 * 
 */
public class MouseOverCommand extends Command {
    public MouseOverCommand() {
    }

    public MouseOverCommand(String command, String target) {
        super(command, target);
    }

    public MouseOverCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public MouseOverCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    /**
     * execute command
     * @return true if the command is executed successfully
     */
    @Override
    public boolean execute() {
        // use selenium to execute the command
        SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
        $.hover();
        return true;
    }

}