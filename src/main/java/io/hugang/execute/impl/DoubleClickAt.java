package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;

public class DoubleClickAt implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "doubleClickAt";
    }

    @Override
    public boolean execute(Command command) {
        SelenideElement $ = CommandExecuteUtil.getElement(command.getTarget());
        String[] offSets = command.getValue().split(",");
        if (ObjectUtil.isNotEmpty(offSets) && offSets.length == 2) {
            int x = Integer.parseInt(offSets[0]);
            int y = Integer.parseInt(offSets[1]);

            ClickOptions clickOptions = ClickOptions.withOffset(x, y);
            $.doubleClick(clickOptions);
            return true;
        }
        return false;
    }
}
