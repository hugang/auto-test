package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

public class EditContentCommand extends Command {
    public EditContentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        SelenideElement element = CommandExecuteUtil.getElement(render(getTarget()));
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("if(arguments[0].contentEditable === 'true') {arguments[0].innerText = '" + render(getValue()) + "'}", element);
        return true;
    }
}
