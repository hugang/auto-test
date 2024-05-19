package io.hugang.execute.condition;

import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;

import java.util.ArrayList;
import java.util.List;

public class TimesCommand extends Command implements IConditionCommand {
    private int times = -1;

    // sub commands
    private List<ICommand> subCommands;
    private String uuid;

    public TimesCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        boolean result = true;
        while (inCondition()) {
            result = result & runSubCommands();
            times--;
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
        if (times == 0) {
            this.setResult(this.getCommand() + ":skip");
        } else {
            this.setResult(this.getCommand() + ":match");
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

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }
}
