package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.WebDriver;

@WebCommand
public class StoreWindowHandleCommand extends Command {
    public StoreWindowHandleCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get driver
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        // get window handle
        String windowHandle = webDriver.getWindowHandle();
        // set variable
        this.setVariable(render(getTarget()), windowHandle);
        return true;
    }
}
