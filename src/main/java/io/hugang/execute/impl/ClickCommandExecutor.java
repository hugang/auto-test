package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

/**
 * click command executor
 * <p>
 *
 * @author hugang
 */
public class ClickCommandExecutor implements CommandExecutor {

    /**
     * execute click command
     * <p> target：selector=xxx or id=xxx
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
        execute($);
        return true;
    }

    /**
     * execute click command
     *
     * @param $ selenide element
     */
    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
