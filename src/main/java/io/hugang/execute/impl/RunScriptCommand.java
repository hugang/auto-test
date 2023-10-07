package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;

public class RunScriptCommand extends Command {
    public RunScriptCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            String value = this.getDictStr("value", this.getValue());
            JavascriptExecutor driver = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            if (this.getTarget() == null) {
                driver.executeScript(value);
            } else {
                SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
                driver.executeScript(value, $);
            }
            // org.openqa.selenium.UnhandledAlertException: unexpected alert open
            try {
                Alert alert = WebDriverRunner.getWebDriver().switchTo().alert();
                alert.accept();
            } catch (NoAlertPresentException e) {
                // do nothing when no alert
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
