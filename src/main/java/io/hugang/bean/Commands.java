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
    String testCase;
    // command list
    List<Command> commands;

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
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
                "testCase='" + testCase + '\'' +
                ", commands=" + commands +
                '}';
    }
}
