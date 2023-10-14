package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;
import io.hugang.util.JavaScriptEvaluator;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class IfCommand extends Command implements IConditionCommand {
    public IfCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean execute() throws CommandExecuteException {
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
        return (boolean) JavaScriptEvaluator.evaluate(render, this.getVariableMap());
    }

    @Override
    public List<ICommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public String toString() {
        return "IfCommand{" +
                "subCommands=" + subCommands +
                '}';
    }
}
