package io.hugang;

import cn.hutool.core.util.StrUtil;
import com.beust.jcommander.JCommander;
import io.hugang.bean.AutoTestCommandLineOption;
import io.hugang.config.AutoTestConfig;

/**
 * entry point to run the program.
 * <p>
 * This class is the entry point of the program.
 *
 * @author hugang
 */
public class RunAutoTest {
    public static void main(String[] args) {
        // parse command line options
        AutoTestCommandLineOption autoTestCommandLineOption = new AutoTestCommandLineOption();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(autoTestCommandLineOption)
                .programName("auto-test")
                .build();
        jCommander.parse(args);

        // if help is true, print help message and return
        if (autoTestCommandLineOption.isHelp()) {
            jCommander.usage();
            return;
        }
        BasicExecutor basicExecutor = new BasicExecutor();
        // set to autoTestConfig if not null
        AutoTestConfig autoTestConfig = basicExecutor.getAutoTestConfig();
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getMode())) {
            autoTestConfig.setTestMode(autoTestCommandLineOption.getMode());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getCases())) {
            autoTestConfig.setTestCases(autoTestCommandLineOption.getCases());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getFilePath())) {
            autoTestConfig.setTestCasePath(autoTestCommandLineOption.getFilePath());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getWorkDir())) {
            autoTestConfig.setWorkDir(autoTestCommandLineOption.getWorkDir());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getBaseDir())) {
            autoTestConfig.setBaseDir(autoTestCommandLineOption.getBaseDir());
        }
        // read the other user properties
        autoTestConfig.readConfigurations();
        // execute the test
        basicExecutor.execute();
    }
}
