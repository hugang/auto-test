package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;

@WebCommand
public class SelectFrameCommand extends Command {
    public SelectFrameCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.switchTo().frame(CommandExecuteUtil.getElement(this.getTarget()));
        return true;
    }
}
