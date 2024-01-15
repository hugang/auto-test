package io.hugang.execute.impl;

import cn.hutool.core.util.StrUtil;
import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;

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
    public boolean _execute() {
        try {
            if (StrUtil.isEmpty(this.getTarget()) || StrUtil.isEmpty(this.getDictStr("value", this.getValue()))) {
                throw new CommandExecuteException("target or value is empty");
            }

            Object target = this.getVariable(this.getTarget());
            String value = this.getDictStr("value", this.getValue());

            if (target instanceof String && ((String) target).contains(",")) {
                Arrays.stream(((String) target).split(",")).forEach(s -> {
                    this.setVariable(value, s);
                    this.runSubCommands();
                });
            } else if (target instanceof List<?> list && !((List<?>) target).isEmpty()) {
                list.forEach(s -> {
                    this.setVariable(value, s);
                    this.runSubCommands();
                });
            } else {
                throw new CommandExecuteException("target is not a list");
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
        return "ForEachCommand{" + "subCommands=" + subCommands + '}';
    }
}
