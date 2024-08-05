package io.hugang.bean;

/**
 * command
 * <p>
 * a command unit to store the command information from input file
 *
 * @author hugang
 */
public class OriginalCommand {
    public OriginalCommand() {
    }

    // command
    private String command;
    // target
    private String target;
    // value
    private String value;
    // comment
    private String comment;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", target='" + target + '\'' +
                ", value='" + value + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
