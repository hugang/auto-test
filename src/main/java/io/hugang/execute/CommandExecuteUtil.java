package io.hugang.execute;

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
        switch (commandType) {
            case "id": {
                return $("#"+commandValue);
            }
            case "css":
            case "selector": {
                return $(commandValue);
            }
            case "linkText": {
                return $(By.linkText(commandValue));
            }
            case "xpath": {
                return $(By.xpath(commandValue));
            }
        }
        return null;
    }
}
