package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
    }

    public ExecuteScriptCommand(String command, String target) {
        super(command, target);
    }

    public ExecuteScriptCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public ExecuteScriptCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            if (ObjectUtil.isNotEmpty(this.getValue())) {
                CommandExecuteUtil.setVariable(this.getValue(), String.valueOf(js.executeScript(this.getTarget())));
            } else {
                js.executeScript(this.getTarget());
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
