package io.hugang.execute.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import io.hugang.CommandExecuteException;
import io.hugang.execute.Command;

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
    private static final Log log = Log.get();
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
    public boolean _execute() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process;

            String value = render(this.getDictStr("value",this.getValue()));
            String type = render(this.getTarget());
            if (StrUtil.isEmpty(type) || StrUtil.isEmpty(value)) {
                throw new CommandExecuteException("type or value is empty");
            }
            switch (type) {
                case TYPE_CMD:
                    process = runtime.exec("cmd /C " + value);
                    break;
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
                // get the input stream of the process TODO: 打印console时乱码
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream(), CharsetUtil.systemCharset()));
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
