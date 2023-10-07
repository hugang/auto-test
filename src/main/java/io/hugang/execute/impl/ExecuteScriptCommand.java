package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.util.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

@WebCommand
public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand(String command, String target, String value) {
        super(command, target, value);
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
