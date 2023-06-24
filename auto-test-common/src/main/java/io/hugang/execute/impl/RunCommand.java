package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * executor for run command
 * <p>
 *
 * @author hugang
 */
public class RunCommand extends Command {
    // log
    private static final Log log = LogFactory.get();
    private static final String BASH_CMD = "/usr/bin/bash";
    private static final String TYPE_CMD = "cmd";
    private static final String TYPE_BAT = "bat";
    private static final String TYPE_SH = "sh";

    public RunCommand(String command, String target, String value) {
        super(command, target, value);
    }

    /**
     * execute command
     * <p>
     * target: file path
     *
     * @return execute result
     */
    @Override
    public boolean execute() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process;

            String value = CommandExecuteUtil.render(this.getValue());
            switch (this.getTarget()) {
                case TYPE_CMD:
                case TYPE_BAT:
                    process = runtime.exec(value);
                    break;
                case TYPE_SH:
                    process = runtime.exec(new String[]{BASH_CMD, FileUtil.file(value).getAbsolutePath()});
                    break;
                default:
                    log.error("unknown command target: {}", this.getTarget());
                    return false;
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
