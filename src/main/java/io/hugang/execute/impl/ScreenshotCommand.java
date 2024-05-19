package io.hugang.execute.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;

public class ScreenshotCommand extends Command {
    private static final Log log = Log.get();

    public ScreenshotCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        int screenshotNumber = 1;
        if (StrUtil.isNotBlank(this.getValue())) {
            screenshotNumber = NumberUtil.parseInt(this.getValue());
            if (screenshotNumber <= 0) {
                screenshotNumber = 1;
            }
        }

        if (screenshotNumber == 1) {
            String screenshot = screenshot(render(this.getTarget()));
            log.info("screenshot: {}", screenshot);
        } else {
            $(By.tagName("body")).sendKeys(Keys.HOME);
            sleep(200);
            for (int i = 0; i < screenshotNumber; i++) {
                String screenshot = screenshot(render(this.getTarget()) + (i + 1));
                log.info("screenshot: {}", screenshot);
                $(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
                sleep(200);
            }
        }

        return true;
    }
}
