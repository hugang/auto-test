package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@WebCommand
public class WebdriverChooseCancelOnVisibleConfirmationCommand extends Command {
    public WebdriverChooseCancelOnVisibleConfirmationCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // get driver
        WebDriver driver = WebDriverRunner.getWebDriver();
        // Wait for the alert to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        // dismiss the prompt dialog
        alert.dismiss();
        return true;
    }
}
