package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * set window size command executor
 * <p>
 *
 * @author hugang
 */
public class SetWindowSizeCommand extends Command {

    public SetWindowSizeCommand() {
    }

    public SetWindowSizeCommand(String command, String target) {
        super(command, target);
    }

    public SetWindowSizeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public SetWindowSizeCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
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
