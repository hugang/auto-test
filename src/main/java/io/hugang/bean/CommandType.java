package io.hugang.bean;

/**
 * command type
 * <p>
 *
 * @author hugang
 */
public enum CommandType {
    // open url
    OPEN("open"),
    // type text
    TYPE("type"),
    // click
    CLICK("click"),
    // select
    SELECT("select"),
    // sleep
    SLEEP("sleep"),
    // take screenshot
    SCREENSHOT("screenshot"),
    // set window size
    SIZE("setWindowSize"),
    // sendKeys
    SENDKEYS("sendKeys");

    CommandType(String command) {
        this.command = command;
    }

    // command name
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * get command type by command name
     *
     * @param command command name
     * @return command type
     */
    public static CommandType parse(String command) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getCommand().equals(command)) {
                return commandType;
            }
        }
        return null;
    }
}
