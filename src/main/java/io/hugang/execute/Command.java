package io.hugang.execute;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.CommandExecuteException;
import io.hugang.config.AutoTestConfig;
import io.hugang.util.CommandExecuteUtil;

/**
 * base command
 * <p>
 * variableMap is for all commands <br>
 * dict is for this command
 */
public abstract class Command implements ICommand {
    private static final Log log = LogFactory.get();

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
    public boolean execute() throws CommandExecuteException {
        this.beforeExecute();
        boolean result = this._execute();
        this.afterExecute();
        return result;
    }

    public abstract boolean _execute() throws CommandExecuteException;

    public void beforeExecute() {

    }

    public void afterExecute() {
        log.info("command: " + this.getCommand() + " execute success");
        if (this.getVariableMap().containsKey("setSpeed")) {
            try {
                Thread.sleep(this.getVariableMap().getInt("setSpeed"));
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
    // variable map
    private Dict variableMap;
    // auto test config
    private AutoTestConfig autoTestConfig;

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

    public Dict getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(Dict variableMap) {
        this.variableMap = variableMap;
    }

    public AutoTestConfig getAutoTestConfig() {
        return autoTestConfig;
    }

    public void setAutoTestConfig(AutoTestConfig autoTestConfig) {
        this.autoTestConfig = autoTestConfig;
    }

    public void appendDict(Dict dict) {
        this.dict.putAll(dict);
    }

    public void appendDict(String key, Object value) {
        this.dict.put(key, value);
    }

    public String render(String value) {
        return CommandExecuteUtil.render(value, this.variableMap);
    }

    public void generateDict() {
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

    public String getFilePath(String options) {
        return CommandExecuteUtil.getFilePath(this.autoTestConfig, options);
    }

    public String getFilePath(String options, boolean isCreate) {
        return CommandExecuteUtil.getFilePath(this.autoTestConfig, options, isCreate);
    }

    public void setVariable(String key, Object value) {
        this.variableMap.put(key, value);
    }

    public Object getVariable(String key) {
        return this.variableMap.get(key);
    }

    public String getVariableStr(String key) {
        return this.variableMap.getStr(key);
    }

    public boolean hasVariable(String key) {
        return this.variableMap.containsKey(key);
    }

    public Dict getVariables() {
        return this.variableMap;
    }
}
