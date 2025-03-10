package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.File;
import java.util.UUID;

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
        String screenshotPath = UUID.randomUUID().toString();
        if (StrUtil.isNotBlank(this.getTarget())) {
            screenshotPath = render(this.getTarget());
        }

        if (screenshotNumber == 1) {
            generateReportData(screenshot(screenshotPath));
            log.info("screenshot: {}", screenshotPath);
        } else {
            $(By.tagName("body")).sendKeys(Keys.HOME);
            sleep(200);
            for (int i = 0; i < screenshotNumber; i++) {
                String screenshot = screenshot(screenshotPath + (i + 1));
                log.info("screenshot: {}", screenshot);
                $(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
                generateReportData(screenshot);
                sleep(200);
            }
        }
        return true;
    }

    private void generateReportData(String imageName) {
        // 添加报告 截图文件路径
        this.appendReport(RESULT_TYPE_MSG, imageName);
        String reportImageName = UUID.randomUUID().toString();
        // 截图文件拷贝至报告文件路径
        String reportImagePath = this.getFilePath(ThreadContext.getAutoTestConfig().getFileDownloadPath().concat("/").concat(this.getReportPath()).concat("/").concat(reportImageName).concat(".png"));
        // 拷贝
        FileUtil.copyFile(FileUtil.file(imageName), new File(reportImagePath));
        // 添加报告 报告文件
        this.appendReport(RESULT_TYPE_IMG, "./".concat(reportImageName).concat(".png"));
    }
}
