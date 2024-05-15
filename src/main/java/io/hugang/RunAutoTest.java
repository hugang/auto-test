package io.hugang;

import cn.hutool.core.util.StrUtil;
import com.beust.jcommander.JCommander;
import io.hugang.bean.AutoTestCommandLineOption;
import io.hugang.config.AutoTestConfig;
import io.hugang.util.ThreadContext;

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
        // if version is not null, print version and return
        if (autoTestCommandLineOption.isVersion()) {
            System.out.println("auto-test version: v1.0");
            return;
        }

        // if filePath is null, print help message and return
        if (StrUtil.isEmpty(autoTestCommandLineOption.getFilePath())) {
            jCommander.usage();
            return;
        }

        BasicExecutor basicExecutor = new BasicExecutor();
        // set to autoTestConfig if not null
        AutoTestConfig autoTestConfig = new AutoTestConfig();
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getMode())) {
            autoTestConfig.setTestMode(autoTestCommandLineOption.getMode());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getCases())) {
            autoTestConfig.setTestCases(autoTestCommandLineOption.getCases());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getFilePath())) {
            autoTestConfig.setTestCasePath(autoTestCommandLineOption.getFilePath());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getBaseDir())) {
            autoTestConfig.setBaseDir(autoTestCommandLineOption.getBaseDir());
        }
        // read the other user properties
        autoTestConfig.readConfigurations();
        ThreadContext.setAutoTestConfig(autoTestConfig);
        // execute the test
        basicExecutor.execute();
    }
}
