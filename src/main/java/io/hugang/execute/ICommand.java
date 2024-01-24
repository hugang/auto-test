package io.hugang.execute;

import cn.hutool.core.lang.Dict;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.config.AutoTestConfig;

public interface ICommand {
    String getCommand();

    boolean execute() throws CommandExecuteException;

    boolean isSkip();

    void setAutoTestConfig(AutoTestConfig autoTestConfig);

    void setVariableMap(Dict variableMap);

    String getResult();
}
