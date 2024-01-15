package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

@WebCommand
public class MouseUpAtCommand extends Command {
    public MouseUpAtCommand(String command, String target, String value) {
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
        String[] offSets = this.getDictStr("value", this.getValue()).split(",");
        if (ObjectUtil.isNotEmpty(offSets) && offSets.length == 2) {
            int x = Integer.parseInt(offSets[0]);
            int y = Integer.parseInt(offSets[1]);
            // perform mouse up action
            actions.moveToElement($, x, y).release().perform();
            return true;
        }
        return false;
    }
}
