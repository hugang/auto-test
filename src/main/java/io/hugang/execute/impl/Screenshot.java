package io.hugang.execute.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;

/**
 * screenshot command executor
 * <p>
 *
 * @author hugang
 */
public class Screenshot implements CommandExecutor {
    private static final Log log = LogFactory.get();

    @Override
    public String getCommandName() {
        return "screenshot";
    }

    /**
     * screenshot command executor
     * <p> target：screenshot file name, value：screenshot number
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        if (StrUtil.isNotBlank(command.getValue())) {
            int screenshotNumber = NumberUtil.parseInt(command.getValue());
            $(By.tagName("body")).sendKeys(Keys.HOME);
            sleep(200);
            for (int i = 0; i < screenshotNumber; i++) {
                String screenshot = screenshot(CommandExecuteUtil.render(command.getTarget()) + (i + 1));
                log.info("screenshot: {}", screenshot);
                $(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
                sleep(200);
            }
            return true;
        }
        return false;
    }
}
