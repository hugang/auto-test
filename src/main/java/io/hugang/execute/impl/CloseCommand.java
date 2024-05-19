package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.OriginalCommand;
import org.openqa.selenium.WebDriver;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class CloseCommand extends Command {
    public CloseCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            WebDriver driver = WebDriverRunner.getWebDriver();
            driver.close();

            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
