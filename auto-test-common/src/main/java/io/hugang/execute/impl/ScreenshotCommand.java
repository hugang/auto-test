package io.hugang.execute.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;

public class ScreenshotCommand extends Command {
    private static final Log log = LogFactory.get();

    public ScreenshotCommand() {
    }

    public ScreenshotCommand(String command, String target) {
        super(command, target);
    }

    public ScreenshotCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public ScreenshotCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        if (StrUtil.isNotBlank(this.getValue())) {
            int screenshotNumber = NumberUtil.parseInt(this.getValue());
            $(By.tagName("body")).sendKeys(Keys.HOME);
            sleep(200);
            for (int i = 0; i < screenshotNumber; i++) {
                String screenshot = screenshot(CommandExecuteUtil.render(this.getTarget()) + (i + 1));
                log.info("screenshot: {}", screenshot);
                $(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
                sleep(200);
            }
            return true;
        }
        return false;
    }
}
