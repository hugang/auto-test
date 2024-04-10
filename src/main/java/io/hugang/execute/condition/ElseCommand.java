package io.hugang.execute.condition;

import io.hugang.util.ThreadContext;

public class ElseCommand extends IfCommand {

    public ElseCommand(String command, String target, String value) {
        super(command, target, value);
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
        return inCondition;
    }
}
