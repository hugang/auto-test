package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebElement;

import java.util.List;

@WebCommand
public class WaitForElementPresentCommand extends Command {
    public WaitForElementPresentCommand(String command, String target, String value) {
        super(command, target, value);
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
                // if element exist, return true
                if (!elements.isEmpty()) {
                    return true;
                }
                Thread.sleep(1000);
            } while (true);
        } catch (Exception e) {
            return false;
        }
    }

}
