package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;

@WebCommand
public class ClickAtCommand extends Command {
    public ClickAtCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
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
