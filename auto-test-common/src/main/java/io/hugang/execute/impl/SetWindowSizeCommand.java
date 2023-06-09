package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
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
        String[] sizes = this.getTarget().split("x");
        int width = Integer.parseInt(sizes[0]);
        int height = Integer.parseInt(sizes[1]);
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.manage().window().setSize(new Dimension(width, height));
        return true;
    }
}
