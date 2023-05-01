package io.hugang.execute.impl;

import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * set window size command executor
 * <p>
 *
 * @author hugang
 */
public class SizeCommandExecutor implements CommandExecutor {
    /**
     * execute set window size command
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        String[] sizes = command.getTarget().split("x");
        int width = Integer.parseInt(sizes[0]);
        int height = Integer.parseInt(sizes[1]);
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.manage().window().setSize(new Dimension(width, height));
        return true;
    }
}
