package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

@WebCommand
public class AssertConfirmation extends Command {
    public AssertConfirmation(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        // get driver
        WebDriver driver = WebDriverRunner.getWebDriver();
        // Switch to the alert dialog
        Alert alert = driver.switchTo().alert();
        // Assert the text in the confirmation dialog
        if (alert.getText().equals(this.getTarget())) {
            // Accept the prompt dialog
            alert.accept();
            // Switch back to the main window
            driver.switchTo().defaultContent();
            return true;
        } else {
            return false;
        }
    }
}
