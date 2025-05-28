package io.hugang.execute.condition;

import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;

import java.util.ArrayList;
import java.util.List;

public class SectionCommand extends Command implements IConditionCommand {
    // sub commands
    private List<ICommand> subCommands;
    private String uuid;

    public SectionCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        boolean result = true;
        while (inCondition()) {
            result = result & runSubCommands();
        }
        return result;
    }

    private boolean runSubCommands() {
        boolean result = true;
        for (ICommand subCommand : this.getSubCommands()) {
            try {
                result = result & subCommand.execute();
            } catch (CommandExecuteException e) {
                throw new CommandExecuteException(e);
            }
        }
        return result;
    }
    @Override
    public boolean inCondition() {
        return true;
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

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }
}
