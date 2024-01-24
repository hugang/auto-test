package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@WebCommand
public class VerifySelectedLabelCommand extends Command {
    private static final Log log = Log.get();

    public VerifySelectedLabelCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // get the element by target
        WebElement element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[@label = '" + render(getValue()) + "']"));
        // if element is null, get the element by title
        if (element == null) {
            element = CommandExecuteUtil.getElement(render(getTarget())).findElement(By.xpath("//option[. = '" + render(getValue()) + "']"));
        }
        // return the selected status of the element
        boolean selected = element.isSelected();

        log.info("verify target: {}, selected label={}", getTarget(), selected);
        return true;
    }
}
