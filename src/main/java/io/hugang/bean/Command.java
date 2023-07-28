package io.hugang.bean;

import io.hugang.CommandExecuteException;
import io.hugang.execute.CommandExecuteUtil;

public abstract class Command implements ICommand {
    public Command() {
    }

    public Command(String command, String target) {
        this.command = command;
        this.target = target;
    }

    public Command(String command, String target, String value) {
        this.command = command;
        this.target = target;
        this.value = value;
    }

    public Command(String command, String description, String target, String value) {
        this.command = command;
        this.description = description;
        this.target = target;
        this.value = value;
    }

    // execute command
    public abstract boolean execute() throws CommandExecuteException;

    // command
    private String command;
    // description
    private String description;
    // target
    private String target;
    // value
    private String value;

    public String getCommand() {
        return command;
    }

    public String getTarget() {
        return target;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String render(String value) {
        return CommandExecuteUtil.render(value);
    }

    @Override
    public String toString() {
        return "Command: " + this.getCommand() + "\t" +
                "Description: " + this.getDescription() + "\t" +
                "Target: " + this.getTarget() + "\t" +
                "Value: " + this.getValue() + "\t";
    }
}
