package io.hugang.execute;

import cn.hutool.core.util.ObjectUtil;
import io.hugang.bean.CommandType;
import io.hugang.execute.impl.*;

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
    private static final Map<CommandType, CommandExecutor> EXECUTORS = new HashMap<>();

    /**
     * get command executor by command name
     *
     * @param command command name
     * @return command executor
     */
    public static CommandExecutor getExecutor(String command) {
        CommandType commandType = CommandType.parse(command);
        return commandType == null ? null : getExecutorByCommandType(commandType);
    }

    /**
     * get command executor by command type
     *
     * @param commandType command type
     * @return command executor
     */
    private static CommandExecutor getExecutorByCommandType(CommandType commandType) {
        if (ObjectUtil.isEmpty(EXECUTORS.get(commandType))) {
            switch (commandType) {
                case OPEN:
                    EXECUTORS.put(commandType, new OpenCommandExecutor());
                    break;
                case CLICK:
                    EXECUTORS.put(commandType, new ClickCommandExecutor());
                    break;
                case TYPE:
                    EXECUTORS.put(commandType, new TypeCommandExecutor());
                    break;
                case SCREENSHOT:
                    EXECUTORS.put(commandType, new ScreenshotCommandExecutor());
                    break;
                case SLEEP:
                    EXECUTORS.put(commandType, new SleepCommandExecutor());
                    break;
                case SELECT:
                    EXECUTORS.put(commandType, new SelectCommandExecutor());
                    break;
                case SIZE:
                    EXECUTORS.put(commandType, new SizeCommandExecutor());
                    break;
                case SENDKEYS:
                    EXECUTORS.put(commandType, new SendKeysCommandExecutor());
                    break;
                case WAITFORTEXT:
                    EXECUTORS.put(commandType, new WaitForTextCommandExecutor());
                    break;
                default:
                    break;
            }
        }
        return EXECUTORS.get(commandType);
    }
}
