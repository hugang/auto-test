package io.hugang.execute.impl;

import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.util.JavaScriptEvaluator;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class IfCommand extends Command implements IConditionCommand {
    public IfCommand() {
    }

    public IfCommand(String command, String target) {
        super(command, target);
    }

    public IfCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public IfCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            if (inCondition()){
                this.subCommands.forEach(ICommand::execute);
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
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
        String render = CommandExecuteUtil.render(this.getTarget());
        return (boolean) JavaScriptEvaluator.evaluate(render, BasicExecutor.variablesMap);
    }

    @Override
    public List<ICommand> getSubCommands() {
        return subCommands;
    }

    public void setSubCommands(List<ICommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public String toString() {
        return "IfCommand{" +
                "subCommands=" + subCommands +
                '}';
    }
}
