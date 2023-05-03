package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
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
            String type = FileUtil.getType(new File(command.getTarget()));
            Runtime runtime = Runtime.getRuntime();
            Process process = null;
            if ("bat".equals(type)) {
                process = runtime.exec(command.getTarget());
            }
            if ("sh".equals(type)) {
                process = runtime.exec("bash " + command.getTarget());
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
