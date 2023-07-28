package io.hugang.execute.impl;

import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;
import io.hugang.execute.JavaScriptEvaluator;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class WhileCommand extends Command implements IConditionCommand {

    public WhileCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            while (inCondition()) {
                this.getSubCommands().forEach(ICommand::execute);
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }

    @Override
    public boolean inCondition() throws ScriptException {
        String render = render(this.getTarget());
        return (boolean) JavaScriptEvaluator.evaluate(render, BasicExecutor.variablesMap);
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
    public String toString() {
        return "WhileCommand{" + "subCommands=" + subCommands + '}';
    }
}
