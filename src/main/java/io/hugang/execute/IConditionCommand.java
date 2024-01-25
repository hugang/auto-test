package io.hugang.execute;

import javax.script.ScriptException;
import java.util.List;

public interface IConditionCommand extends ICommand {
    boolean inCondition() throws ScriptException;

    List<ICommand> getSubCommands();

    void addSubCommand(ICommand subCommand);

    void setUuid(String uuid);

    String getUuid();

}
