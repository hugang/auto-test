package io.hugang;

public class CommandExecuteException extends RuntimeException {
    public CommandExecuteException(Exception e) {
        super(e);
    }
}
