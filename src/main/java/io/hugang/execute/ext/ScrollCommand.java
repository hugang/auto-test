package io.hugang.execute.ext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.annotation.ReportCommand;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

/**
 * scroll command
 * <p>
 * scroll window
 * <br>
 * usage: scroll | target
 */
@WebCommand
@ReportCommand
public class ScrollCommand extends Command {
    private static final Log log = Log.get();

    public ScrollCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "scroll";
    }

    @Override
    public boolean _execute() {
        try {
            String target = this.render(this.getTarget());
            String value = this.render(this.getValue());
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            if (StrUtil.isEmpty(target)) {
                // targetが空の場合、valueを使用してスクロール
                int scrollValue = StrUtil.isEmpty(value) ? 0 : Integer.parseInt(value);
                js.executeScript("window.scrollTo(0, " + scrollValue + ")");
            } else {
                SelenideElement element = CommandExecuteUtil.getElement(target);
                int scrollValue = StrUtil.isEmpty(value) ? 0 : Integer.parseInt(value);
                js.executeScript("arguments[0].scrollTop = arguments[1];", element, scrollValue);
            }
            return true;
        } catch (Exception e) {
            log.error("scroll command execute error", e);
            return false;
        }
    }
}
