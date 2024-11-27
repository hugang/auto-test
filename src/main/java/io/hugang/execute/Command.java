package io.hugang.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.AutoTestException;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.ThreadContext;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * base command
 * <p>
 * variableMap is for all commands <br>
 * dict is for this command
 */
public abstract class Command implements ICommand {
    protected static final Log log = Log.get();

    public static final String TARGET = "target";
    public static final String VALUE = "value";

    public static final String RESULT_TYPE_MSG = "msg";
    public static final String RESULT_TYPE_IMG = "img";

    public Command() {
    }

    public Command(OriginalCommand originalCommand) {
        this.command = originalCommand.getCommand();
        this.target = originalCommand.getTarget();
        this.value = originalCommand.getValue();
        this.comment = originalCommand.getComment();
        generateDict();
    }

    // execute command
    public boolean execute() {
        String cmd = this.getCommand();

        // when command is click,open, and the value is 0 or skip, then skip
        if (!(cmd.equals("type") || cmd.equals("storeJson") || cmd.equals("sendKeys"))
                && StrUtil.isNotEmpty(getValue())
                && ("0".equals(getValue()) || "skip".equalsIgnoreCase(getValue()))) {
            this.setResult(cmd.concat(":skip"));
            return true;
        }

        try {
            this.beforeExecute();
            boolean result = this._execute();
            this.afterExecute();

            if (StrUtil.isEmpty(this.getResult())) {
                if (result) {
                    this.setResult(cmd.concat(":success"));
                } else {
                    this.setResult(cmd.concat(":fail"));
                }
            }
            return result;
        } catch (CommandExecuteException e) {
            this.setResult(cmd.concat(":" + e.getMessage()));
            throw new AutoTestException(e);
        }
    }

    public abstract boolean _execute() throws CommandExecuteException;

    public void beforeExecute() {
    }

    public void afterExecute() {
        log.debug("command: " + this.getCommand() + " execute success");
        if (ThreadContext.getVariables().containsKey("setSpeed")) {
            try {
                Thread.sleep(ThreadContext.getVariables().getInt("setSpeed"));
            } catch (InterruptedException e) {
                throw new CommandExecuteException(e);
            }
        }
        // generate report
        String reportPath = ThreadContext.getReportPath().concat("/").concat(this.getReportPath());
        File reportFile = FileUtil.mkdir(reportPath);
        FileUtil.appendString("## ".concat(this.getCommand()).concat("\n\n"), reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());

        Dict resultDict = Dict.create();
        resultDict.put(VALUE, this.getValue());
        resultDict.put(TARGET, this.getTarget());
        resultDict.put("comment", this.getComment());

        FileUtil.appendString("```json\n".concat(JSONUtil.toJsonPrettyStr(resultDict)).concat("\n```\n\n"), reportFile.getAbsolutePath().concat("/report.md"), Charset.defaultCharset());

        // additional report
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

    // command
    private String command;
    // dict to store variables
    private final Dict dict = new Dict();
    // target
    private String target;
    // value
    private String value;
    // comment
    private String comment;
    // result
    private String result;
    // report
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

    public void appendReport(String type, String info) {
        if (this.report == null) {
            this.report = new ArrayList<>();
        }
        CommandReport report = new CommandReport();
        report.setType(type);
        report.setInfo(info);
        this.report.add(report);
    }

    public String getReportPath() {
        return ThreadContext.getReportUuid().concat("_").concat(this.getVariableStr("caseId"));
    }

    public void appendDict(Dict dict) {
        this.dict.putAll(dict);
    }

    public void appendDict(String key, Object value) {
        this.dict.put(key, value);
    }

    public String render(String value) {
        return CommandExecuteUtil.render(value, ThreadContext.getVariables());
    }

    public String render(String value, Dict dict) {
        return CommandExecuteUtil.render(value, dict);
    }

    public void generateDict() {
        try {
            if (this.target != null) {
                JSONObject targetObj = (JSONObject) JSONUtil.parse(this.target);
                targetObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // do nothing if not json
            log.debug(this.target + " : not a valid json string: {}, process as normal target.", e.getMessage());
        }
        try {
            if (this.value != null) {
                JSONObject valueObj = (JSONObject) JSONUtil.parse(this.value);
                valueObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // do nothing if not json
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

    public String getFilePath(String path) {
        return CommandExecuteUtil.getFilePath(path);
    }

    public String getFilePath(String options, boolean isCreate) {
        return CommandExecuteUtil.getFilePath(options, isCreate);
    }

    public void setVariable(String key, Object value) {
        ThreadContext.getVariables().put(key, value);
    }

    public Object getVariable(String key) {
        return ThreadContext.getVariables().get(key);
    }

    public String getVariableStr(String key) {
        return ThreadContext.getVariables().getStr(key);
    }

    public boolean hasVariable(String key) {
        return ThreadContext.getVariables().containsKey(key);
    }

    @JsonIgnore
    public Dict getVariables() {
        return ThreadContext.getVariables();
    }



    public static class CommandReport {
        private String type;
        private String info;

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
