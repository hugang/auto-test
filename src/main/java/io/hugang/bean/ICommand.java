package io.hugang.bean;

import io.hugang.CommandExecuteException;

public interface ICommand {
    boolean execute() throws CommandExecuteException;
    boolean isSkip();
}
