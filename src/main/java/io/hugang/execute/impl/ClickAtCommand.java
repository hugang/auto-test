package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class ClickAtCommand extends Command {
    public ClickAtCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        SelenideElement $ = CommandExecuteUtil.getElement(getTarget());
        String value = this.getDictStr("offSet", getDictStr("value", getValue()));
        String[] offSets = value.split(",");
        if (ObjectUtil.isNotEmpty(offSets) && offSets.length == 2) {
            int x = Integer.parseInt(offSets[0]);
            int y = Integer.parseInt(offSets[1]);

            ClickOptions clickOptions = ClickOptions.withOffset(x, y);
            $.click(clickOptions);
            return true;
        }
        return false;
    }
}
