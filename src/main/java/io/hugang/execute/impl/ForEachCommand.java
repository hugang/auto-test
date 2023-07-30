package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;
import io.hugang.execute.CommandExecuteUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForEachCommand extends Command implements IConditionCommand {

    public ForEachCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean execute() throws CommandExecuteException {
        try {
            Object target = CommandExecuteUtil.getVariable(this.getTarget());
            String value = this.getDictStr("value", this.getValue());

            System.out.println(target.getClass());
            if (target instanceof String && ((String) target).contains(",")) {
                Arrays.stream(((String) target).split(",")).forEach(s -> {
                    CommandExecuteUtil.setVariable(value, s);
                    this.getSubCommands().forEach(ICommand::execute);
                });
            } else if (target instanceof List) {
                ((List) target).forEach(s -> {
                    CommandExecuteUtil.setVariable(value, s);
                    this.getSubCommands().forEach(ICommand::execute);
                });
            } else {
                throw new CommandExecuteException("target is not a list");
            }
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }

    @Override
    public boolean inCondition() {
        return true;
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
