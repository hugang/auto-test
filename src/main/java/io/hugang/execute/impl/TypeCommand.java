package io.hugang.execute.impl;

import io.hugang.annotation.WebCommand;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;
import org.openqa.selenium.Keys;

@WebCommand
public class TypeCommand extends Command {

    public static final String VALUE = "value";

    public TypeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        String value = this.getDictStr(VALUE, getValue());
        CommandExecuteUtil.getElement(this.getTarget()).setValue(render(value).replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));
        return true;
    }
}
