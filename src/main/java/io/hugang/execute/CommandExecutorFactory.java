package io.hugang.execute;

import cn.hutool.core.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * factory class for generate command executor
 * <p>
 *
 * @author hugang
 */
public class CommandExecutorFactory {
    // map to store command executor
    private static final Map<String, CommandExecutor> EXECUTORS = new HashMap<>();

    static {
        // scan all class and get the class which implements CommandExecutor interface
        // and put them into EXECUTORS map
        ClassUtil.scanPackage("io.hugang.execute.impl", (clazz) -> {
            if (CommandExecutor.class.isAssignableFrom(clazz)) {
                try {
                    CommandExecutor commandExecutor = (CommandExecutor) clazz.newInstance();
                    EXECUTORS.put(commandExecutor.getCommandName(), commandExecutor);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }

    /**
     * get command executor by command name
     *
     * @param command command name
     * @return command executor
     */
    public static CommandExecutor getExecutor(String command) {
        return EXECUTORS.get(command);
    }

}
