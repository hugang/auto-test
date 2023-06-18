package io.hugang.execute.impl;

import com.codeborne.selenide.SelenideElement;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * wait for text command executor
 * <p>
 *
 * @author hugang
 */
public class WaitForTextCommand extends Command {
    public WaitForTextCommand() {
    }

    public WaitForTextCommand(String command, String target) {
        super(command, target);
    }

    public WaitForTextCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public WaitForTextCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    /**
     * execute wait for text command
     *
     * @return success or not
     */
    @Override
    public boolean execute() throws CommandExecuteException {
        int timeout = 0;
        try {
            do {
                SelenideElement $ = CommandExecuteUtil.getElement(this.getTarget());
                if (existValue($.innerText())) {
                    return true;
                }
                if (existValue($.innerHtml())) {
                    return true;
                }
                if (existValue($.getText())) {
                    return true;
                }
                if (existValue($.getValue())) {
                    return true;
                }
                timeout += 1000;
                // wait for 1 hour
                if (timeout > 60 * 60 * 1000) {
                    return false;
                }
                Thread.sleep(1000);
            } while (true);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * exist value
     *
     * @param value   value
     * @return exist or not
     */
    private boolean existValue(String value) {
        return null != value && value.contains(CommandExecuteUtil.render(this.getValue()));
    }
}
