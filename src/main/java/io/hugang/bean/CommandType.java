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
    SEND_KEYS("sendKeys"),
    // wait for text
    WAIT_FOR_TEXT("waitForText"),
    // run
    RUN("run"),
    // set property
    SET_PROPERTY("setProperty"),
    // set element value to property
    SET_ELEMENT_TO_PROPERTY("setElementToProperty"),
    // read properties
    READ_PROPERTIES("readProperties"),
    // save properties
    SAVE_PROPERTIES("saveProperties"),
    // run jenkins job
    JENKINS_JOB("jenkinsJob"),
    // increase number
    INCREASE_NUMBER("increaseNumber"),
    ;

    CommandType(String command) {
        this.command = command;
    }

    // command name
    private final String command;

    public String getCommand() {
        return command;
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
