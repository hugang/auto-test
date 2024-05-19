package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@WebCommand
public class WebdriverAnswerOnVisiblePromptCommand extends Command {
    public WebdriverAnswerOnVisiblePromptCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        // get driver
        WebDriver driver = WebDriverRunner.getWebDriver();
        // Wait for the alert to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        // Input the value
        alert.sendKeys(render(this.getTarget()));
        // Accept the prompt dialog
        alert.accept();
        return true;
    }
}
