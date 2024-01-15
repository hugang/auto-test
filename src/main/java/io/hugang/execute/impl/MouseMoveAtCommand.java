package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.interactions.Actions;

@WebCommand
public class MouseMoveAtCommand extends Command {
    public MouseMoveAtCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute command
     *
     * @return true if the command is executed successfully
     */
    @Override
    public boolean execute() {
        Actions actions = new Actions(WebDriverRunner.getWebDriver());

        // use selenium to execute the command
        SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
        String[] offSets = this.getDictStr("value", this.getValue()).split(",");
        if (ObjectUtil.isNotEmpty(offSets) && offSets.length == 2) {
            int x = Integer.parseInt(offSets[0]);
            int y = Integer.parseInt(offSets[1]);
            // perform mouse up action
            actions.moveToElement($, x, y).perform();
            return true;
        }
        return false;
    }

}
