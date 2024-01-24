package io.hugang.exceptions;

public class AutoTestException extends RuntimeException {
    public AutoTestException(Exception e) {
        super(e);
    }

    public AutoTestException(String message) {
        super(message);
    }
}
