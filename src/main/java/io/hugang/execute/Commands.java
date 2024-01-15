package io.hugang.execute;

import java.util.List;

/**
 * command collection
 * <p>
 *
 * @author hugang
 */
public class Commands {
    // test case id
    private String caseId;
    private boolean webCommand;
    // command list
    private List<ICommand> commands;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public void setCommands(List<ICommand> commands) {
        this.commands = commands;
    }

    public boolean isWebCommand() {
        return webCommand;
    }

    public void setWebCommand(boolean webCommand) {
        this.webCommand = webCommand;
    }
}
