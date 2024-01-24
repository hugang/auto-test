package io.hugang.bean;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExecutionCaseResultDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<ExecutionCommandResultDetail> commands;
    private String result;

    public ExecutionCaseResultDetail(List<ExecutionCommandResultDetail> commands, String result) {
        this.commands = commands;
        this.result = result;
    }

    public List<ExecutionCommandResultDetail> getCommands() {
        return commands;
    }

    public void setCommands(List<ExecutionCommandResultDetail> commands) {
        this.commands = commands;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void appendCommandResultDetail(String commandName, String result) {
        if (commands == null) {
            commands = new ArrayList<>();
        }
        commands.add(new ExecutionCommandResultDetail(commandName, result));
    }
}