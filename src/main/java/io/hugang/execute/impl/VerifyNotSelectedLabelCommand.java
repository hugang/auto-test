package io.hugang.execute.impl;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@WebCommand
public class VerifyNotSelectedLabelCommand extends Command {
    private static final Log log = LogFactory.get();

    public VerifyNotSelectedLabelCommand(String command, String target, String value) {
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
        boolean selected = element.isSelected();
        log.info("verify target: {}, not selected={}", getTarget(), !selected);
        return true;
    }
}
