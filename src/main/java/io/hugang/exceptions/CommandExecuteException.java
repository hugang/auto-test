package io.hugang.exceptions;

public class CommandExecuteException extends RuntimeException {
    public CommandExecuteException(Exception e) {
        super(e);
    }

    public CommandExecuteException(String message) {
        super(message);
    }
}
