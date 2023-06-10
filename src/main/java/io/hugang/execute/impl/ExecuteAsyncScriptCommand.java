package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

public class ExecuteAsyncScriptCommand extends Command {
    public ExecuteAsyncScriptCommand() {
    }

    public ExecuteAsyncScriptCommand(String command, String target) {
        super(command, target);
    }

    public ExecuteAsyncScriptCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public ExecuteAsyncScriptCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        if (ObjectUtil.isNotEmpty(this.getValue())) {
            CommandExecuteUtil.setVariable(this.getValue(), String.valueOf(js.executeAsyncScript(this.getTarget())));
        } else {
            js.executeAsyncScript(this.getTarget());
        }
        return true;
    }
}
