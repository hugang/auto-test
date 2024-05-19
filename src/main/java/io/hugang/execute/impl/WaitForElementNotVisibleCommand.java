package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebElement;

import java.util.List;

@WebCommand
public class WaitForElementNotVisibleCommand extends Command {
    public WaitForElementNotVisibleCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        int timeout = 0;
        try {
            do {
                List<WebElement> elements = CommandExecuteUtil.findElements(this.getTarget());
                String timeoutValue = this.getDictStr("value", this.getValue());
                long timeoutLong = Long.parseLong(timeoutValue);
                timeout += 1000;
                if (timeout > timeoutLong) {
                    return false;
                }
                // if element exist, and element visible return true
                if (elements.isEmpty() || !elements.get(0).isDisplayed()) {
                    return true;
                }
                Thread.sleep(1000);
            } while (true);
        } catch (Exception e) {
            return false;
        }
    }
}
