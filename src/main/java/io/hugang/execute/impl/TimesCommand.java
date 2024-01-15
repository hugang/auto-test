package io.hugang.execute.impl;

import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;

import java.util.ArrayList;
import java.util.List;

public class TimesCommand extends Command implements IConditionCommand {
    private int times = -1;

    public TimesCommand(String command, String target, String value) {
        super(command, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean _execute() {
        while (inCondition()) {
            runSubCommands();
            times--;
        }
        return true;
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
        if (times < 0) {
            this.times = Integer.parseInt(render(this.getTarget()));
            // loop limit
            String value = this.getDictStr("value", this.getValue());
            if (value != null) {
                int limit = Integer.parseInt(render(value));
                if (times > limit) {
                    times = limit;
                }
            }
        }
        return times > 0;
    }

    @Override
    public List<ICommand> getSubCommands() {
        return this.subCommands;
    }

    @Override
    public void addSubCommand(ICommand subCommand) {
        if (this.subCommands == null) {
            this.subCommands = new ArrayList<>();
        }
        this.subCommands.add(subCommand);
    }
}
