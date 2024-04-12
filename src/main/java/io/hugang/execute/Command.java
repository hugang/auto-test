package io.hugang.execute;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.exceptions.AutoTestException;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.ThreadContext;

/**
 * base command
 * <p>
 * variableMap is for all commands <br>
 * dict is for this command
 */
public abstract class Command implements ICommand {
    protected static final Log log = Log.get();

    public static final String TARGET = "target";

    public Command() {
    }

    public Command(String command, String target) {
        this.command = command;
        this.target = target;
        generateDict();
    }

    public Command(String command, String target, String value) {
        this.command = command;
        this.target = target;
        this.value = value;
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
        log.info("command: " + this.getCommand() + " execute success");
        if (ThreadContext.getVariables().containsKey("setSpeed")) {
            try {
                Thread.sleep(ThreadContext.getVariables().getInt("setSpeed"));
            } catch (InterruptedException e) {
                throw new CommandExecuteException(e);
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
    // result
    private String result;

    @Override
    public String getCommand() {
        return command;
    }

    public String getTarget() {
        return this.getDictStr(TARGET, this.target);
    }

    public String getValue() {
        return value;
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
    private void generateDict() {
        try {
            if (this.target != null) {
                JSONObject targetObj = (JSONObject) JSONUtil.parse(this.target);
                targetObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // do nothing if not json
        }
        try {
            if (this.value != null) {
                JSONObject valueObj = (JSONObject) JSONUtil.parse(this.value);
                valueObj.forEach((key, value) -> this.appendDict(key, value));
            }
        } catch (Exception e) {
            // do nothing if not json
        }
    }

    @Override
    public boolean isSkip() {
        return this.getDict().containsKey("skip") && this.getDict().getBool("skip");
    }

    @Override
    public String toString() {
        return "Command: " + this.getCommand() + "\t" + "options: " + this.getDict() + "\t" + "Target: " + this.getTarget() + "\t" + "Value: " + this.getValue() + "\t";
    }

    public String getFilePath(String path) {
        return CommandExecuteUtil.getFilePath(ThreadContext.getAutoTestConfig(), path);
    }

    public String getFilePath(String options, boolean isCreate) {
        return CommandExecuteUtil.getFilePath(ThreadContext.getAutoTestConfig(), options, isCreate);
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

    public Dict getVariables() {
        return ThreadContext.getVariables();
    }
}
