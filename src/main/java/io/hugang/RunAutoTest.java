package io.hugang;

import cn.hutool.core.util.StrUtil;
import com.beust.jcommander.JCommander;
import io.hugang.bean.AutoTestCommandLineOption;

/**
 * entry point to run the program.
 * <p>
 * This class is the entry point of the program.
 *
 * @author hugang
 */
public class RunAutoTest {
    public static void main(String[] args) {

        AutoTestCommandLineOption autoTestCommandLineOption = new AutoTestCommandLineOption();
        JCommander.newBuilder()
                .addObject(autoTestCommandLineOption)
                .build()
                .parse(args);

        BasicExecutor basicExecutor = new BasicExecutor();
        // set to autoTestConfig if not null
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getMode())) {
            basicExecutor.getAutoTestConfig().setTestMode(autoTestCommandLineOption.getMode());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getTestCases())) {
            basicExecutor.getAutoTestConfig().setTestCases(autoTestCommandLineOption.getTestCases());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getPath())) {
            basicExecutor.getAutoTestConfig().setTestCasePath(autoTestCommandLineOption.getPath());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getWorkDir())) {
            basicExecutor.getAutoTestConfig().setWorkDir(autoTestCommandLineOption.getWorkDir());
        }
        if (StrUtil.isNotEmpty(autoTestCommandLineOption.getCurrentDir())) {
            basicExecutor.getAutoTestConfig().setCurrentDir(autoTestCommandLineOption.getCurrentDir());
        }
        basicExecutor.execute();
    }
}
