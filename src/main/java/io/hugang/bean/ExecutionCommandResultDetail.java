package io.hugang.bean;

import java.io.Serial;
import java.io.Serializable;

public class ExecutionCommandResultDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String commandName;
    private String result;

    public ExecutionCommandResultDetail(String commandName, String result) {
        this.commandName = commandName;
        this.result = result;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
