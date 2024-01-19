package io.hugang.execute.impl;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.JavaScriptEvaluator;
import org.openqa.selenium.WebElement;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class IfCommand extends Command implements IConditionCommand {
    public IfCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;
    private String uuid;

    @Override
    public boolean _execute() {
        try {
            if (inCondition()) {
                runSubCommands();
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }

    private void runSubCommands() {
        this.getSubCommands().forEach(e -> {
            try {
                e.setVariableMap(this.getVariableMap());
                e.setAutoTestConfig(this.getAutoTestConfig());
                e.execute();
            } catch (CommandExecuteException ex) {
                throw new CommandExecuteException(ex);
            }
        });
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
        boolean inCondition = (boolean) JavaScriptEvaluator.evaluate(render, this.getVariableMap())
                && !this.getVariableMap().containsKey(uuid);
        log.info(this.getCommand() + " command execute, matching : {}", inCondition);

        if (inCondition) {
            this.getVariableMap().put(uuid, uuid);
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
