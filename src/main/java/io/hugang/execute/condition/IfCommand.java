package io.hugang.execute.condition;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.JavaScriptEvaluator;
import io.hugang.util.ThreadContext;
import org.openqa.selenium.WebElement;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class IfCommand extends Command implements IConditionCommand {

    // sub commands
    private List<ICommand> subCommands;
    private String uuid;

    public IfCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            if (inCondition()) {
                return runSubCommands();
            }
            return true;
        } catch (ScriptException e) {
            throw new CommandExecuteException(e);
        }
    }

    private boolean runSubCommands() {
        boolean result = true;
        for (ICommand subCommand : this.getSubCommands()) {
            try {
                result = result & subCommand.execute();
            } catch (CommandExecuteException e) {
                throw new CommandExecuteException(e);
            }
        }
        return result;
    }

    @Override
    public void addSubCommand(ICommand subCommand) {
        if (this.subCommands == null) {
            this.subCommands = new ArrayList<>();
        }
        this.subCommands.add(subCommand);
    }

    @Override
    public boolean inCondition() throws ScriptException {
        String render = render(this.getTarget());
        if (render.startsWith("xpath=") || render.startsWith("css=") || render.startsWith("id=")) {
            List<WebElement> elements = CommandExecuteUtil.findElements(render);
            return ObjectUtil.isNotEmpty(elements);
        }
        boolean inCondition = (boolean) JavaScriptEvaluator.evaluate(render, ThreadContext.getVariableMap())
                && !ThreadContext.getVariableMap().containsKey(uuid);
        log.info(this.getCommand() + " command execute, matching : {}", inCondition);

        if (inCondition) {
            ThreadContext.getVariableMap().put(uuid, uuid);
            this.setResult(this.getCommand() + ":match");
        }else {
            this.setResult(this.getCommand() + ":skip");
        }
        return inCondition;
    }

    @Override
    public void afterExecute() {
    }

    @Override
    public List<ICommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "IfCommand{" +
                "subCommands=" + subCommands +
                '}';
    }
}
