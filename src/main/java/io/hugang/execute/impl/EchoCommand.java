package io.hugang.execute.impl;

import cn.hutool.log.Log;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;

public class EchoCommand extends Command {
    public EchoCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    private static final Log log = Log.get();

    @Override
    public boolean _execute() {
        String target = this.getTarget();
        String value = this.getValue();

        if (target != null) {
            String renderedTarget = render(target);
            log.info(renderedTarget);
            this.appendReport(RESULT_TYPE_MSG,renderedTarget);
            return true;
        } else if (value != null) {
            String renderedValue = render(value);
            log.info(renderedValue);
            this.appendReport(RESULT_TYPE_MSG,renderedValue);
            return true;
        } else {
            log.error("Both target and value are null");
            return false;
        }
    }
}
