package io.hugang.execute;

import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.BasicExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * command execute util
 * <p>
 *
 * @author hugang
 */
public class CommandExecuteUtil {
    private static final Log log = LogFactory.get();
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
        if ($ == null) {
            log.info("can not find element by target: {}", target);
        }
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
     * set variable
     *
     * @param key key
     * @param $   selenide element
     */
    public static void setVariable(String key, SelenideElement $) {
        BasicExecutor.variablesMap.put(key, CommandExecuteUtil.getElementValue($));
    }

    private static String getElementValue(SelenideElement $) {
        String value = null;
        if ($ != null) {
            switch ($.getTagName()) {
                case "input": {
                    value = $.getValue();
                    break;
                }
                case "select": {
                    value = $.getSelectedOptionText();
                    break;
                }
                default: {
                    value = $.getText();
                    break;
                }
            }
        }
        return value;
    }

    /**
     * set variable
     *
     * @param key   key
     * @param value value
     */
    public static void setVariable(String key, String value) {
        BasicExecutor.variablesMap.put(key, value);
    }

    /**
     * get variable
     *
     * @param key key
     * @return variable
     */
    public static String getVariable(String key) {
        return BasicExecutor.variablesMap.get(key);
    }

    /**
     * has variable
     *
     * @param key key
     * @return has or not
     */
    public static boolean hasVariable(String key) {
        return BasicExecutor.variablesMap.containsKey(key);
    }

    /**
     * render template
     *
     * @param value template
     * @return rendered value
     */
    public static String render(String value) {
        return ENGINE.getTemplate(value).render(BasicExecutor.variablesMap);
    }
}
