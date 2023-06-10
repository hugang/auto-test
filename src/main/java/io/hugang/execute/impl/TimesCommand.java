package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;

import java.util.ArrayList;
import java.util.List;

public class TimesCommand extends Command implements IConditionCommand {
    private int times = Integer.parseInt(this.getTarget());

    public TimesCommand() {
    }

    public TimesCommand(String command, String target) {
        super(command, target);
    }

    public TimesCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public TimesCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    // sub commands
    private List<ICommand> subCommands;

    @Override
    public boolean execute() {
        while (inCondition()) {
            this.subCommands.forEach(ICommand::execute);
            times--;
        }
        return true;
    }

    @Override
    public boolean inCondition() {
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
