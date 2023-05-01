package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.Keys;

/**
 * send keys command executor
 * <p>
 *
 * @author hugang
 */
public class SendKeysCommandExecutor implements CommandExecutor {
    /**
     * execute send keys command
     * <p> target：keys
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        execute(CommandExecuteUtil.getElement(command.getTarget()), command.getValue());
        return true;
    }

    /**
     * execute click command
     *
     * @param $ selenide element
     */
    private void execute(SelenideElement $, String keys) {
        if ($.isDisplayed() && $.isEnabled()) {
            if (keys.startsWith("${KEY_") && keys.endsWith("}")) {
                $.sendKeys(Keys.valueOf(keys.replace("${KEY_", "").replace("}", "")));
            } else {
                $.sendKeys(keys);
            }
        }
    }
}
