package io.hugang.execute.impl;

import cn.hutool.core.util.StrUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.WebDriver;

import java.util.Set;

@WebCommand
public class SelectWindowCommand extends Command {
    public SelectWindowCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        // 获取当前窗口句柄
        String currentHandle = "";
        try {
            currentHandle = driver.getWindowHandle();
        } catch (Exception e) {
            // ignore
        }
        // 获取所有窗口句柄
        Set<String> handles = driver.getWindowHandles();
        if (!handles.isEmpty() && StrUtil.isEmpty(currentHandle)) {
            currentHandle = (String) handles.toArray()[0];
            driver.switchTo().window(currentHandle);
            return true;
        }
        // 切换到新窗口
        for (int i = 0; i < handles.size(); i++) {
            String handle = (String) handles.toArray()[i];
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                String title = driver.getTitle();
                String currentUrl = driver.getCurrentUrl();
                if (title.contains(getTarget()) || currentUrl.contains(getTarget()) || "tab=".concat(String.valueOf(i)).equals(getTarget())) {
                    return true;
                }
            }
        }

        return false;
    }
}
