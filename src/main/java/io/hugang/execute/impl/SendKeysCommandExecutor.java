package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

/**
 * send keys command executor
 * <p>
 *
 * @author hugang
 */
public class SendKeysCommandExecutor implements io.hugang.execute.CommandExecutor {
    /**
     * execute send keys command
     * <p> target：keys
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        int index = command.getTarget().indexOf("=");
        String commandType = command.getTarget().substring(0, index);
        String commandValue = command.getTarget().substring(index + 1);
        switch (commandType) {
            case "id": {
                execute($("#" + commandValue), command.getValue());
                break;
            }
            case "css":
            case "selector": {
                execute($(commandValue), command.getValue());
                break;
            }
            case "linkText": {
                execute($(By.linkText(commandValue)), command.getValue());
                break;
            }
        }
        return true;
    }

    /**
     * execute click command
     *
     * @param $ selenide element
     */
    private void execute(SelenideElement $, String keys) {
        if ($.isDisplayed() && $.isEnabled()) {
            if (keys.startsWith("${KEY_") && keys.endsWith("}")){
                $.sendKeys(Keys.valueOf(keys.replace("${KEY_", "").replace("}", "")));
            }else {
                $.sendKeys(keys);
            }
        }
    }
}
