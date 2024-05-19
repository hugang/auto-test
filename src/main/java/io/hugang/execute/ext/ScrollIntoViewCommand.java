package io.hugang.execute.ext;

import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

/**
 * scroll into view command
 * <p>
 * scroll element into view
 * <br>
 * usage: scrollIntoView | target
 */
public class ScrollIntoViewCommand extends Command {
    private static final Log log = Log.get();

    @Override
    public String getCommand() {
        return "scrollIntoView";
    }

    @Override
    public boolean _execute() {
        try {
            // get from variable map
            String target = this.render(this.getTarget());
            // get element by target
            SelenideElement element = CommandExecuteUtil.getElement(target);
            // scroll into view
            element.scrollIntoView(true);
            return true;
        } catch (Exception e) {
            log.error("increase number command execute error", e);
            return false;
        }
    }
}
