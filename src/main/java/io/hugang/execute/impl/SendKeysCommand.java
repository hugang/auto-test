package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.Keys;

/**
 * send keys command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SendKeysCommand extends Command {
    public SendKeysCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    /**
     * execute send keys command
     * <p> target：keys
     *
     * @return success or not
     */
    @Override
    public boolean _execute() {
        execute(CommandExecuteUtil.getElement(this.getTarget()), this.getDictStr("value", this.getValue()));
        return true;
    }

    /**
     * execute click command
     *
     * @param $ selenide element
     */
    private void execute(SelenideElement $, String keys) {
        if ($.isEnabled()) {
            if (keys.startsWith("${KEY_") && keys.endsWith("}")) {
                $.sendKeys(Keys.valueOf(keys.replace("${KEY_", "").replace("}", "")));
            } else {
                $.sendKeys(keys);
            }
        }
    }
}
