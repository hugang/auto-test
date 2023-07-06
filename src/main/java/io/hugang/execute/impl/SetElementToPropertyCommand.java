package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * set element to property command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SetElementToPropertyCommand extends Command {
    public SetElementToPropertyCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * set element to property
     *
     * @return execute result
     */
    @Override
    public boolean execute() {
        SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
        // set $ to variable maps
        CommandExecuteUtil.setVariable(this.getValue(), $);
        return true;
    }
}
