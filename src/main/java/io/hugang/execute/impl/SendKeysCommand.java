package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.Keys;

/**
 * send keys command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SendKeysCommand extends Command {
    public SendKeysCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute send keys command
     * <p> target：keys
     *
     * @return success or not
     */
    @Override
    public boolean execute() {
        execute(CommandExecuteUtil.getElement(this.getTarget()), this.getValue());
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
