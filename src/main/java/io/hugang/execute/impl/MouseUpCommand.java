package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

@WebCommand
public class MouseUpCommand extends Command {
    public MouseUpCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get web driver
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        // create action
        Actions actions = new Actions(webDriver);
        // use selenium to execute the command
        SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
        // perform mouse up action
        actions.moveToElement($).release().perform();
        return true;
    }
}
