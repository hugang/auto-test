package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;

@WebCommand
public class SelectFrameCommand extends Command {
    public SelectFrameCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.switchTo().frame(CommandExecuteUtil.getElement(this.getTarget()));
        return true;
    }
}
