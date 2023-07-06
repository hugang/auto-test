package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.bean.ICommand;
import io.hugang.bean.IConditionCommand;
import io.hugang.execute.CommandExecuteUtil;

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
    public boolean execute() {
        while (inCondition()) {
            this.getSubCommands().forEach(ICommand::execute);
            times--;
        }
        return true;
    }

    @Override
    public boolean inCondition() {
        if (times < 0) {
            this.times = Integer.parseInt(CommandExecuteUtil.render(this.getTarget()));
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
