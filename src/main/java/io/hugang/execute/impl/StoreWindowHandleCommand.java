package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.WebDriver;

@WebCommand
public class StoreWindowHandleCommand extends Command {

    public StoreWindowHandleCommand(String command, String target, String value) {
        super(command, target, value);
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
