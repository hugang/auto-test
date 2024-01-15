package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

@WebCommand
public class VerifyElementPresentCommand extends Command {
    private static final Log log = LogFactory.get();

    public VerifyElementPresentCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        List<WebElement> elements = CommandExecuteUtil.findElements(this.getTarget());
        if (ObjectUtil.isEmpty(elements)) {
            log.info("element not found: {}", this.getTarget());
        } else {
            log.info("element found: {}", this.getTarget());
            // scroll to element when found
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            js.executeScript("arguments[0].scrollIntoView();", elements.get(0));
        }
        return true;
    }
}
