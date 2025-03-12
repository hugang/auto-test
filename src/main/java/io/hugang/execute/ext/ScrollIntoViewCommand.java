package io.hugang.execute.ext;

import cn.hutool.log.Log;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.ReportCommand;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

/**
 * scroll into view command
 * <p>
 * scroll element into view
 * <br>
 * usage: scrollIntoView | target
 */
@WebCommand
@ReportCommand
public class ScrollIntoViewCommand extends Command {
    private static final Log log = Log.get();

    public ScrollIntoViewCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

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
            log.error("scroll into view error", e);
            return false;
        }
    }
}
