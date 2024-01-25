package io.hugang.execute.condition;

import io.hugang.exceptions.CommandExecuteException;
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
            boolean result = true;
            while (inCondition()) {
                result = result & this.runSubCommands();
            }
            return result;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }

    private boolean runSubCommands() {
        boolean result = true;
        for (ICommand subCommand : this.getSubCommands()) {
            try {
                subCommand.setVariableMap(this.getVariableMap());
                subCommand.setAutoTestConfig(this.getAutoTestConfig());
                result = result & subCommand.execute();
            } catch (CommandExecuteException e) {
                throw new CommandExecuteException(e);
            }
        }
        return result;
    }

    @Override
    public boolean inCondition() throws ScriptException {
        String render = render(this.getTarget());
        boolean evaluate = (boolean) JavaScriptEvaluator.evaluate(render, this.getVariableMap());
        if (evaluate) {
            this.setResult(this.getCommand() + ":match");
        } else {
            this.setResult(this.getCommand() + ":skip");
        }
        return evaluate;
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
