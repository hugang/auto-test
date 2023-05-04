package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * executor for run command
 * <p>
 *
 * @author hugang
 */
public class RunCommandExecutor implements CommandExecutor {
    // log
    private static final Log log = LogFactory.get();
    public static final String BAT_SUFFIX = "bat";
    public static final String SH_SUFFIX = "sh";
    public static final String BASH_CMD = "bash ";

    /**
     * execute command
     * <p>
     * target: file path
     *
     * @param command command
     * @return execute result
     */
    @Override
    public boolean execute(Command command) {
        try {
            // run bat file
            boolean isFile = FileUtil.isFile(command.getTarget());
            String type = StrUtil.EMPTY;
            if (isFile) {
                type = FileUtil.getType(new File(command.getTarget()));
            }
            Runtime runtime = Runtime.getRuntime();
            Process process;
            if (BAT_SUFFIX.equals(type)) {
                process = runtime.exec(command.getTarget());
            } else if (SH_SUFFIX.equals(type)) {
                process = runtime.exec(BASH_CMD + command.getTarget());
            } else {
                process = runtime.exec(command.getTarget());
            }
            if (process != null) {
                // get the input stream of the process
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                // read the output line by line
                String line;
                while ((line = input.readLine()) != null) {
                    // print the output to the console
                    log.info(line);
                }
                // close the input stream
                input.close();
                process.waitFor();
                process.destroy();
                return process.exitValue() == 0;
            }
        } catch (IOException | InterruptedException e) {
            log.error("execute command error", e);
            return false;
        }
        return false;
    }
}
