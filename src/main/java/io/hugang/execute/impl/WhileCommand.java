package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;
import io.hugang.util.JavaScriptEvaluator;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class WhileCommand extends Command implements IConditionCommand {

    public WhileCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;
    private String uuid;

    @Override
    public boolean _execute() {
        try {
            while (inCondition()) {
                this.runSubCommands();
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
    public boolean inCondition() throws ScriptException {
        String render = render(this.getTarget());
        return (boolean) JavaScriptEvaluator.evaluate(render, this.getVariableMap());
    }

    @Override
    public List<ICommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void addSubCommand(ICommand subCommand) {
        if (this.subCommands == null) {
            this.subCommands = new ArrayList<>();
        }
        this.subCommands.add(subCommand);
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
        return "WhileCommand{" + "subCommands=" + subCommands + '}';
    }
}
