package io.hugang.execute;

import io.hugang.exceptions.CommandExecuteException;

public interface ICommand {
    String getCommand();

    boolean execute() throws CommandExecuteException;

    boolean isSkip();

    String getResult();
}
