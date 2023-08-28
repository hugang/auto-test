package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

@WebCommand
public class MouseUpCommand extends Command {
    public MouseUpCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
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
