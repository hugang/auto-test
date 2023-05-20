package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.JavascriptExecutor;

public class ExecuteScript implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "executeScript";
    }

    @Override
    public boolean execute(Command command) {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        if (ObjectUtil.isNotEmpty(command.getValue())) {
            CommandExecuteUtil.setVariable(command.getValue(), String.valueOf(js.executeScript(command.getTarget())));
        } else {
            js.executeScript(command.getTarget());
        }
        return false;
    }
}
