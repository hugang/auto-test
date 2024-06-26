package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

/**
 * EditContentCommand
 * <p>
 * 注意必须满足contentEditable属性为true的条件，如
 * <div class="ql-editor" contenteditable="true">
 */
@WebCommand
public class EditContentCommand extends Command {
    public EditContentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
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
