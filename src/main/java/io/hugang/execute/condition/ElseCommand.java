package io.hugang.execute.condition;

import io.hugang.bean.OriginalCommand;
import io.hugang.util.ThreadContext;

public class ElseCommand extends IfCommand {

    public ElseCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean inCondition() {
        String uuid = this.getUuid();
        boolean inCondition = !ThreadContext.getVariableMap().containsKey(uuid);
        log.info(this.getCommand() + " command execute, matching : {}", inCondition);

        if (inCondition) {
            ThreadContext.getVariableMap().put(uuid, uuid);
            this.setResult(this.getCommand() + ":match");
        } else {
            this.setResult(this.getCommand() + ":skip");
        }
        this.appendReport(RESULT_TYPE_MSG, this.getTarget());
        return inCondition;
    }
}
