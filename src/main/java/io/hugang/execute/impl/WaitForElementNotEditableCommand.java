package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@WebCommand
public class WaitForElementNotEditableCommand extends Command {
    public WaitForElementNotEditableCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // Get the WebDriver instance
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        // Wait for the element to become non-editable
        new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)).until(
                ExpectedConditions.not(ExpectedConditions.elementToBeClickable(CommandExecuteUtil.getElement(render(this.getTarget()))))
        );
        return true;
    }
}
