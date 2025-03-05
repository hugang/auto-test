package io.hugang.bean;

/**
 * 命令类，用于存储从输入文件中解析出的命令信息。
 * <p>
 * 该类作为一个数据容器，存储命令的名称、目标、值和注释。
 *
 * @author hugang
 */
public class OriginalCommand {

    /**
     * 默认构造函数，用于创建 OriginalCommand 对象。
     */
    public OriginalCommand() {
    }

    // 命令名称或类型
    private String command;

    // 命令的目标（例如，操作的元素或路径）
    private String target;

    // 命令的值（例如，输入的内容或参数）
    private String value;

    // 命令的注释或描述信息
    private String comment;

    /**
     * 获取命令名称。
     *
     * @return 命令名称
     */
    public String getCommand() {
        return command;
    }

    /**
     * 设置命令名称。
     *
     * @param command 命令名称
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * 获取命令的目标。
     *
     * @return 命令的目标
     */
    public String getTarget() {
        return target;
    }

    /**
     * 设置命令的目标。
     *
     * @param target 命令的目标
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 获取命令的值。
     *
     * @return 命令的值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置命令的值。
     *
     * @param value 命令的值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取命令的注释。
     *
     * @return 命令的注释
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置命令的注释。
     *
     * @param comment 命令的注释
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 重写 toString 方法，返回对象的字符串表示形式。
     *
     * @return 对象的字符串表示形式
     */
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