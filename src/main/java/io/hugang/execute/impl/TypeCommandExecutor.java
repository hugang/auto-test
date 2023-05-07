package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import io.hugang.execute.CommandExecutor;
import org.openqa.selenium.Keys;

/**
 * type command executor
 * <p>
 *
 * @author hugang
 */
public class TypeCommandExecutor implements CommandExecutor {
    @Override
    public String getCommandName() {
        return "type";
    }

    /**
     * type command executor
     * <p> target：selector or id, value：input value
     *
     * @param command command
     * @return success or not
     */
    @Override
    public boolean execute(Command command) {
        // replace \n to shift+enter
        CommandExecuteUtil.getElement(command.getTarget()).setValue(CommandExecuteUtil.render(command.getValue()).replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));
        return true;
    }
}
