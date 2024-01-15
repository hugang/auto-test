package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.interactions.Actions;

@WebCommand
public class DragAndDropToObjectCommand extends Command {

    public DragAndDropToObjectCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // source element
        SelenideElement sourceElement = CommandExecuteUtil.getElement(this.getTarget());
        // target element
        SelenideElement targetElement = CommandExecuteUtil.getElement(this.getValue());
        // drag and drop
        new Actions(WebDriverRunner.getWebDriver()).dragAndDrop(sourceElement, targetElement).perform();
        return true;


    }
}
