package io.hugang.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hugang.annotation.ReportCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.ThreadContext;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.codeborne.selenide.Selenide.screenshot;

/**
 * 基础命令类，所有具体命令的父类。
 * <p>
 * variableMap 用于存储所有命令的全局变量，dict 用于存储当前命令的局部变量。
 */
public abstract class Command implements ICommand {
    // 日志对象
    protected static final Log log = Log.get();

    // 常量定义
    public static final String TARGET = "target"; // 目标字段
    public static final String VALUE = "value";   // 值字段
    public static final String RESULT_TYPE_MSG = "msg"; // 报告类型：消息
    public static final String RESULT_TYPE_IMG = "img"; // 报告类型：图片

    // 默认构造函数
    public Command() {
    }

    // 带参数的构造函数，用于初始化命令
    public Command(OriginalCommand originalCommand) {
        this.command = originalCommand.getCommand(); // 设置命令名称
        this.target = originalCommand.getTarget();   // 设置目标
        this.value = originalCommand.getValue();     // 设置值
        this.comment = originalCommand.getComment(); // 设置注释
        generateDict(); // 解析 target 和 value 生成字典
    }

    /**
     * 执行命令的主方法。
     *
     * @return 命令执行结果，true 表示成功，false 表示失败
     */
    public boolean execute() {
        String cmd = this.getCommand();

        // 如果命令是 click、open 等，并且 value 为 "0" 或 "skip"，则跳过执行
        if (!(cmd.equals("type") || cmd.equals("storeJson") || cmd.equals("sendKeys"))
                && StrUtil.isNotEmpty(getValue())
                && ("0".equals(getValue()) || "skip".equalsIgnoreCase(getValue()))) {
            this.setResult(cmd.concat(":skip")); // 设置跳过结果
            return true;
        }

        this.beforeExecute(); // 执行前的准备工作
        boolean result=false;
        try {
            result = this._execute(); // 执行具体命令逻辑
        } catch (CommandExecuteException e) {
            this.setResult(cmd.concat(":" + e.getMessage())); // 设置异常结果
            this.appendReport(RESULT_TYPE_MSG,e.getMessage());
        }
        this.afterExecute();  // 执行后的清理工作

        // 如果结果为空，根据执行结果设置成功或失败
        if (StrUtil.isEmpty(this.getResult())) {
            if (result) {
                this.setResult(cmd.concat(":success")); // 设置成功结果
            } else {
                this.setResult(cmd.concat(":fail")); // 设置失败结果
            }
        }
        return result;
    }

    /**
     * 抽象方法，子类必须实现该方法来执行具体的命令逻辑。
     *
     * @return 命令执行结果，true 表示成功，false 表示失败
     * @throws CommandExecuteException 命令执行异常
     */
    public abstract boolean _execute() throws CommandExecuteException;

    /**
     * 命令执行前的准备工作，默认实现为空。
     */
    public void beforeExecute() {
        generateReportImage("before"); // 生成报告用图片
    }

    /**
     * 命令执行后的清理工作，包括日志记录和报告生成。
     */
    public void afterExecute() {
        log.debug("command: " + this.getCommand() + " execute success"); // 记录日志

        generateReportImage("after"); // 生成报告用图片

        // 如果设置了执行速度，则休眠相应时间
        if (ThreadContext.getVariables().containsKey("setSpeed")) {
            try {
                Thread.sleep(ThreadContext.getVariables().getInt("setSpeed"));
            } catch (InterruptedException e) {
                throw new CommandExecuteException(e);
            }
        }

        // 生成报告
        String reportPath = ThreadContext.getReportPath().concat("/").concat(this.getReportSubPath());
        File reportFile = FileUtil.mkdir(reportPath); // 创建报告目录
        FileUtil.appendString("## ".concat(this.getCommand()).concat("\n\n"), reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());

        // 将命令的目标、值、注释写入报告
        Dict resultDict = Dict.create();
        resultDict.put(VALUE, this.getValue());
        resultDict.put(TARGET, this.getTarget());
        resultDict.put("comment", this.getComment());

        FileUtil.appendString("```json\n".concat(JSONUtil.toJsonPrettyStr(resultDict)).concat("\n```\n\n"), reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());

        // 如果有额外的报告信息，写入报告
        if (ObjectUtil.isNotEmpty(this.getReport())) {
            for (CommandReport commandReport : this.getReport()) {
                if (RESULT_TYPE_MSG.equals(commandReport.getType())) {
                    FileUtil.appendString("- ".concat(commandReport.getInfo()).concat("\n\n"), reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());
                }
                if (RESULT_TYPE_IMG.equals(commandReport.getType())) {
                    FileUtil.appendString("![".concat(commandReport.getInfo()).concat("]")
                                    .concat("(").concat(commandReport.getInfo()).concat(")").concat("\n\n"),
                            reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());
                }
            }
        }
    }

    private void generateReportImage(String type) {
        // 生成报告用图片
        if (this.getClass().isAnnotationPresent(ReportCommand.class)){
            String reportType = this.getClass().getAnnotation(ReportCommand.class).value();
            if (!type.equals(reportType)) {
                return;
            }
            String reportImageName = UUID.randomUUID().toString();
            screenshot(this.getReportSubPath().concat("/").concat(reportImageName));
            this.appendReport(RESULT_TYPE_IMG, "./".concat(reportImageName).concat(".png"));
        }
    }

    // 命令名称
    private String command;
    // 字典，用于存储命令的局部变量
    private final Dict dict = new Dict();
    // 目标
    private String target;
    // 值
    private String value;
    // 注释
    private String comment;
    // 结果
    private String result;
    // 报告列表
    private List<CommandReport> report;

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getTarget() {
        return this.getDictStr(TARGET, this.target);
    }

    @Override
    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Dict getDict() {
        return dict;
    }

    public Object getDict(String key) {
        return dict.get(key);
    }

    public String getDictStr(String key) {
        return dict.getStr(key);
    }

    public String getDictStr(String key, String defaultValue) {
        String value = dict.getStr(key);
        return StrUtil.isEmptyIfStr(value) ? defaultValue : value;
    }

    @Override
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<CommandReport> getReport() {
        return report;
    }

    public void setReport(List<CommandReport> report) {
        this.report = report;
    }

    /**
     * 向报告中添加新的条目。
     *
     * @param type 报告类型（如 msg 或 img）
     * @param info 报告信息
     */
    public void appendReport(String type, String info) {
        if (this.report == null) {
            this.report = new ArrayList<>();
        }
        CommandReport report = new CommandReport();
        report.setType(type);
        report.setInfo(info);
        this.report.add(report);
    }

    /**
     * 获取报告文件的路径。
     *
     * @return 报告文件路径
     */
    public String getReportSubPath() {
        return "CaseId".concat(this.getVariableStr("caseId")).concat("_").concat(ThreadContext.getReportUuid());
    }

    /**
     * 向字典中添加多个键值对。
     *
     * @param dict 要添加的字典
     */
    public void appendDict(Dict dict) {
        this.dict.putAll(dict);
    }

    /**
     * 向字典中添加单个键值对。
     *
     * @param key   键
     * @param value 值
     */
    public void appendDict(String key, Object value) {
        this.dict.put(key, value);
    }

    /**
     * 渲染字符串，替换其中的变量。
     *
     * @param value 要渲染的字符串
     * @return 渲染后的字符串
     */
    public String render(String value) {
        return CommandExecuteUtil.render(value, ThreadContext.getVariables());
    }

    /**
     * 渲染字符串，替换其中的变量。
     *
     * @param value 要渲染的字符串
     * @param dict  用于替换的字典
     * @return 渲染后的字符串
     */
    public String render(String value, Dict dict) {
        return CommandExecuteUtil.render(value, dict);
    }

    /**
     * 解析 target 和 value 属性，将其转换为字典中的键值对。
     */
    public void generateDict() {
        try {
            if (this.target != null) {
                JSONObject targetObj = (JSONObject) JSONUtil.parse(this.target);
                targetObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // 如果 target 不是有效的 JSON 字符串，则忽略
            log.debug(this.target + " : not a valid json string: {}, process as normal target.", e.getMessage());
        }
        try {
            if (this.value != null) {
                JSONObject valueObj = (JSONObject) JSONUtil.parse(this.value);
                valueObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // 如果 value 不是有效的 JSON 字符串，则忽略
            log.debug(this.value + " : not a valid json string: {}, process as normal value.", e.getMessage());
        }
    }

    @Override
    public boolean isSkip() {
        return this.getDict().containsKey("skip") && this.getDict().getBool("skip");
    }

    @Override
    public String toString() {
        return "Command: " + this.getCommand() + "\t" + "options: " + this.getDict() + "\t" + "Target: " + this.getTarget() + "\t" + "Value: " + this.getValue() + "\t" + "comment: " + this.getComment();
    }

    /**
     * 获取文件路径。
     *
     * @param path 文件路径
     * @return 解析后的文件路径
     */
    public String getFilePath(String path) {
        return CommandExecuteUtil.getFilePath(path);
    }

    /**
     * 获取文件路径。
     *
     * @param options  文件路径选项
     * @param isCreate 是否创建文件
     * @return 解析后的文件路径
     */
    public String getFilePath(String options, boolean isCreate) {
        return CommandExecuteUtil.getFilePath(options, isCreate);
    }

    /**
     * 设置全局变量。
     *
     * @param key   变量名
     * @param value 变量值
     */
    public void setVariable(String key, Object value) {
        ThreadContext.getVariables().put(key, value);
    }

    /**
     * 获取全局变量。
     *
     * @param key 变量名
     * @return 变量值
     */
    public Object getVariable(String key) {
        return ThreadContext.getVariables().get(key);
    }

    /**
     * 获取全局变量的字符串形式。
     *
     * @param key 变量名
     * @return 变量值的字符串形式
     */
    public String getVariableStr(String key) {
        return ThreadContext.getVariables().getStr(key);
    }

    /**
     * 检查是否存在指定的全局变量。
     *
     * @param key 变量名
     * @return 如果存在返回 true，否则返回 false
     */
    public boolean hasVariable(String key) {
        return ThreadContext.getVariables().containsKey(key);
    }

    @JsonIgnore
    public Dict getVariables() {
        return ThreadContext.getVariables();
    }

    /**
     * 内部类，用于存储命令执行结果的报告信息。
     */
    public static class CommandReport {
        private String type; // 报告类型
        private String info; // 报告信息

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}