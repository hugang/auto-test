package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

@WebCommand
public class CloseCommand extends Command {
    public CloseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            WebDriver driver = WebDriverRunner.getWebDriver();
            driver.close();

            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
