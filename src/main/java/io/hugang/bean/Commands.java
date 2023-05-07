package io.hugang.bean;

import java.util.List;

/**
 * command collection
 * <p>
 *
 * @author hugang
 */
public class Commands {
    // test case id
    String caseId;
    // command list
    List<Command> commands;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "Commands{" +
                "caseId='" + caseId + '\'' +
                ", commands=" + commands +
                '}';
    }
}
