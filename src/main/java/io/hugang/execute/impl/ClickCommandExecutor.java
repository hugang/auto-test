package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

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
        int index = command.getTarget().indexOf("=");
        String commandType = command.getTarget().substring(0, index);
        String commandValue = command.getTarget().substring(index + 1);
        switch (commandType) {
            case "id": {
                execute($("#" + commandValue));
                break;
            }
            case "css":
            case "selector": {
                execute($(commandValue));
                break;
            }
            case "linkText": {
                execute($(By.linkText(commandValue)));
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
    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
