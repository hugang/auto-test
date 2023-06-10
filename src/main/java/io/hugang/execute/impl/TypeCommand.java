package io.hugang.execute.impl;

import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.Keys;

public class TypeCommand extends Command {

    public TypeCommand() {
    }

    public TypeCommand(String command, String target) {
        super(command, target);
    }

    public TypeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public TypeCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    @Override
    public boolean execute() {
        CommandExecuteUtil.getElement(this.getTarget()).setValue(CommandExecuteUtil.render(this.getValue()).replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));
        return true;
    }
}
