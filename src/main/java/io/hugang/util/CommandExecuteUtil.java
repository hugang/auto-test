package io.hugang.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.config.AutoTestConfig;
import io.hugang.exceptions.CommandExecuteException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * command execute util
 * <p>
 *
 * @author hugang
 */
public class CommandExecuteUtil {
    private static final Log log = Log.get();
    private static final int TIMEOUT = 10000;
    private static int spendTime = 0;
    private static final TemplateEngine ENGINE = TemplateUtil.createEngine();

    /**
     * get element by target
     *
     * @param target target
     * @return selenide element
     */
    public static SelenideElement getElement(String target) {
        int indexOf = target.indexOf("=");
        String commandType = target.substring(0, indexOf);
        String commandValue = target.substring(indexOf + 1);
        SelenideElement $ = null;
        switch (commandType) {
            case "id": {
                $ = $("#" + commandValue);
                break;
            }
            case "css":
            case "selector": {
                $ = $(commandValue);
                break;
            }
            case "linkText": {
                $ = $(By.linkText(commandValue));
                break;
            }
            case "xpath": {
                $ = $(By.xpath(commandValue));
                break;
            }
        }
        assert $ != null;
        if (!$.exists() && spendTime > TIMEOUT) {
            log.info("can not find element by target: {}", target);
            throw new CommandExecuteException("can not find element by target: " + target);
        } else if (!$.exists()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("sleep error", e);
            }
            spendTime += 1000;
            return getElement(target);
        }
        spendTime = 0;
        return $;
    }

    public static List<WebElement> findElements(String target) {
        int indexOf = target.indexOf("=");
        String commandType = target.substring(0, indexOf);
        String commandValue = target.substring(indexOf + 1);
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        List<WebElement> $ = null;
        switch (commandType) {
            case "id": {
                $ = webDriver.findElements(By.cssSelector("#" + commandValue));
                break;
            }
            case "css":
            case "selector": {
                $ = webDriver.findElements(By.cssSelector(commandValue));
                break;
            }
            case "linkText": {
                $ = webDriver.findElements(By.linkText(commandValue));
                break;
            }
            case "xpath": {
                $ = webDriver.findElements(By.xpath(commandValue));
                break;
            }
        }
        if ($ == null) {
            log.info("can not find element by target: {}", target);
        }
        return $;
    }

    /**
     * render template
     *
     * @param value template
     * @return rendered value
     */
    public static String render(Dict dict, String value) {
        return ENGINE.getTemplate(value).render(dict);
    }

    public static String render(String value, Dict dict) {
        return ENGINE.getTemplate(value).render(dict);
    }

    public static String getFilePath(AutoTestConfig autoTestConfig, String path, boolean createIfNotExists) {
        File file;
        if (FileUtil.isAbsolutePath(path)) {
            file = FileUtil.file(path);
        } else {
            file = FileUtil.file(autoTestConfig.getBaseDir() + path);
        }
        if (!file.exists() && createIfNotExists) {
            FileUtil.touch(file);
        }
        return file.getAbsolutePath();
    }

    public static String getFilePath(AutoTestConfig autoTestConfig, String path) {
        return getFilePath(autoTestConfig, path, false);
    }
}
