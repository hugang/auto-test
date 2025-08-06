package io.hugang.execute.ext;

import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

@WebCommand
public class ClickIfPresentCommand extends Command {
    public ClickIfPresentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "clickIfPresent";
    }


    @Override
    public boolean _execute() {
        List<WebElement> elements = CommandExecuteUtil.findElements(render(getTarget()));
        if (elements.size() == 1) {
            execute($(elements.get(0)));
        }
        return true;
    }

    private void execute(SelenideElement $) {
        if ($.isDisplayed() && $.isEnabled()) {
            $.click();
        }
    }
}
