package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;

@WebCommand
public class SelectFrameCommand extends Command {
    public SelectFrameCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.switchTo().frame(CommandExecuteUtil.getElement(this.getTarget()));
        return true;
    }
}
