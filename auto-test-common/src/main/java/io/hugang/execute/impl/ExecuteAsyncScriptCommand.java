package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.JavascriptExecutor;

@WebCommand
public class ExecuteAsyncScriptCommand extends Command {
    public ExecuteAsyncScriptCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            if (ObjectUtil.isNotEmpty(this.getValue())) {
                CommandExecuteUtil.setVariable(this.getValue(), String.valueOf(js.executeAsyncScript(this.getTarget())));
            } else {
                js.executeAsyncScript(this.getTarget());
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
