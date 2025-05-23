package io.hugang.execute.ext;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

@WebCommand
public class TypeIfPresentCommand extends Command {

    public static final String VALUE = "value";

    public TypeIfPresentCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "typeIfPresent";
    }


    @Override
    public boolean _execute() {
        List<WebElement> elements = CommandExecuteUtil.findElements(render(getTarget()));
        if (elements.size() == 1) {
            String value = this.getDictStr(VALUE, getValue());
            elements.get(0).sendKeys(render(value).replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));
        }
        return true;
    }
}
