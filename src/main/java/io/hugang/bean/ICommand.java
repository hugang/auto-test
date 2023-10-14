package io.hugang.bean;

import cn.hutool.core.lang.Dict;
import io.hugang.CommandExecuteException;
import io.hugang.config.AutoTestConfig;

public interface ICommand {
    boolean execute() throws CommandExecuteException;
    boolean isSkip();
    void setAutoTestConfig(AutoTestConfig autoTestConfig);
    void setVariableMap(Dict variableMap);
}
