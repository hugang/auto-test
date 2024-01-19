package io.hugang.execute.impl;

public class ElseCommand extends IfCommand {

    public ElseCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean inCondition() {
        String uuid = this.getUuid();
        boolean inCondition = !this.getVariableMap().containsKey(uuid);
        log.info(this.getCommand() + " command execute, matching : {}", inCondition);

        if (inCondition) {
            this.getVariableMap().put(uuid, uuid);
        }
        return inCondition;
    }
}
