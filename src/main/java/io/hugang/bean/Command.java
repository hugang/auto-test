package io.hugang.bean;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.hugang.CommandExecuteException;
import io.hugang.execute.CommandExecuteUtil;

public abstract class Command implements ICommand {

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
    public abstract boolean execute() throws CommandExecuteException;

    // command
    private String command;
    // dict to store variables
    private final Dict dict = new Dict();
    // target
    private String target;
    // value
    private String value;

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

    public void appendDict(Dict dict) {
        this.dict.putAll(dict);
    }

    public void appendDict(String key, Object value) {
        this.dict.put(key, value);
    }

    public String render(String value) {
        return CommandExecuteUtil.render(value);
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
}
