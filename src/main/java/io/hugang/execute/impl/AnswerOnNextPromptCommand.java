package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

@WebCommand
public class AnswerOnNextPromptCommand extends Command {
    public AnswerOnNextPromptCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        // get driver
        WebDriver driver = WebDriverRunner.getWebDriver();
        // Switch to the alert dialog
        Alert alert = driver.switchTo().alert();
        // Enter the text to be entered in the prompt dialog
        alert.sendKeys(render(this.getTarget()));
        // Accept the prompt dialog
        alert.accept();
        // Switch back to the main window
        driver.switchTo().defaultContent();
        return true;
    }
}