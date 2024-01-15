package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * set window size command executor
 * <p>
 *
 * @author hugang
 */
@WebCommand
public class SetWindowSizeCommand extends Command {
    public SetWindowSizeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute set window size command
     *
     * @return success or not
     */
    @Override
    public boolean execute() {
        if (!this.getTarget().contains("x") && !"max".equals(this.getTarget()) && !"min".equals(this.getTarget())) {
            return false;
        }
        try {
            WebDriver driver = WebDriverRunner.getWebDriver();
            if ("max".equals(this.getTarget())) {
                driver.manage().window().maximize();
                return true;
            }
            if ("min".equals(this.getTarget())) {
                driver.manage().window().setSize(new Dimension(0, 0));
            } else {
                String[] sizes = this.getTarget().split("x");
                int width = Integer.parseInt(sizes[0]);
                int height = Integer.parseInt(sizes[1]);
                driver.manage().window().setSize(new Dimension(width, height));
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
