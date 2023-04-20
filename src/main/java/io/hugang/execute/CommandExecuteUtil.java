package io.hugang.execute;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * command execute util
 * <p>
 *
 * @author hugang
 */
public class CommandExecuteUtil {
    private static final Log log = LogFactory.get();

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
}
