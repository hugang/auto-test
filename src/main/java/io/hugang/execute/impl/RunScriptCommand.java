package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;

@WebCommand
public class RunScriptCommand extends Command {
    public RunScriptCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
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
