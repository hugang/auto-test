package io.hugang.execute.ext;

import cn.hutool.log.Log;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.JavascriptExecutor;

/**
 * scroll command
 * <p>
 * scroll window
 * <br>
 * usage: scroll | target
 */
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
            // get from variable map
            String target = this.render(this.getTarget());
            // use js to scroll, the target is the scroll height
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            js.executeScript("window.scrollTo(0, " + target + ")");
            return true;
        } catch (Exception e) {
            log.error("increase number command execute error", e);
            return false;
        }
    }
}
