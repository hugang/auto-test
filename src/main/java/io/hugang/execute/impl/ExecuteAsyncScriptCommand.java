package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;
import org.openqa.selenium.JavascriptExecutor;

@WebCommand
public class ExecuteAsyncScriptCommand extends Command {
    public ExecuteAsyncScriptCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            if (ObjectUtil.isNotEmpty(this.getValue())) {
                this.setVariable(this.getValue(), String.valueOf(js.executeAsyncScript(this.getTarget())));
            } else {
                js.executeAsyncScript(this.getTarget());
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }
}
