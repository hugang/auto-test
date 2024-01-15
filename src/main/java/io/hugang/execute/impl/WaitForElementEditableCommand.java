package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@WebCommand
public class WaitForElementEditableCommand extends Command {
    public WaitForElementEditableCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        new WebDriverWait(webDriver, Duration.of(10, ChronoUnit.SECONDS)).until(
                ExpectedConditions.elementToBeClickable(CommandExecuteUtil.getElement(render(this.getTarget())))
        );
        return true;
    }
}
