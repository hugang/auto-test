package io.hugang.execute;

import io.hugang.exceptions.CommandExecuteException;

public interface ICommand {

    void setCommand(String command);
    String getCommand();
    boolean execute() throws CommandExecuteException;
    boolean isSkip();
    String getResult();

    void setTarget(String target);
    String getTarget();
    void setValue(String value);
    String getValue();
    void setComment(String comment);
    String getComment();
}
