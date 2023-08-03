package io.hugang.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * auto test config
 *
 * @author hugang
 */
public class AutoTestConfig {
    // config group of browser
    public static final String GROUP_BROWSER = "browser";
    // config group of test case
    public static final String GROUP_TEST_CASE = "test-case";
    // config group of base
    public static final String GROUP_BASE = "base";
    public static final String AUTO_TEST_HOME = "AUTO_TEST_HOME";

    /**
     * read auto-test.conf
     */
    public void readConfigurations() {
        // read auto-test.conf
        File file = FileUtil.file(this.getBaseDir() + "conf/auto-test.conf");
        if (!file.exists()) {
            return;
        }
        Setting setting = SettingUtil.get(file.getAbsolutePath());
        String userProfilePath = setting.get(GROUP_BROWSER, "user.profile.path");
        this.setUserProfilePath(getAbsolutePath(userProfilePath));

        String webDriverName = setting.get(GROUP_BROWSER, "web.driver");
        if (webDriverName != null) {
            this.setWebDriverName(webDriverName);
        }

        String webDriverPath = setting.get(GROUP_BROWSER, "web.driver.path");
        this.setWebDriverPath(getAbsolutePath(webDriverPath, this.getBaseDir()));

        if (StrUtil.isEmpty(this.getTestCasePath())) {
            String testCasePath = setting.get(GROUP_TEST_CASE, "test.case.path");
            this.setTestCasePath(getAbsolutePath(testCasePath));
        }

        String fileDownloadPath = setting.get(GROUP_BASE, "file.download.path");
        this.setFileDownloadPath(getAbsolutePath(fileDownloadPath));

        String width = setting.get(GROUP_BROWSER, "browser.width");
        if (width != null) {
            this.setWidth(Integer.parseInt(width));
        }
        String height = setting.get(GROUP_BROWSER, "browser.height");
        if (height != null) {
            this.setHeight(Integer.parseInt(height));
        }
        if (StrUtil.isEmpty(this.getTestMode())) {
            String inputMedia = setting.get(GROUP_TEST_CASE, "test.case.mode");
            if (inputMedia != null) {
                this.setTestMode(inputMedia);
            }
        }
        if (StrUtil.isEmpty(this.getTestCases())) {
            String testCases = setting.get(GROUP_TEST_CASE, "xlsx.specific.testcases");
            if (testCases != null) {
                this.setTestCases(testCases);
            }
        }
        String cronEnabled = setting.get(GROUP_BASE, "cron.enabled");
        if (cronEnabled != null) {
            this.setCronEnabled(Boolean.parseBoolean(cronEnabled));
        }
        String cronExpression = setting.get(GROUP_BASE, "cron.expression");
        if (cronExpression != null) {
            this.setCronExpression(cronExpression);
        }
        String browserBinaryPath = setting.get(GROUP_BROWSER, "browser.binary.path");
        if (browserBinaryPath != null) {
            this.setBrowserBinaryPath(browserBinaryPath);
        }
        // restart driver by case
        String restartDriverByCase = setting.get(GROUP_BROWSER, "web.driver.restartByCase");
        if (restartDriverByCase != null) {
            this.setRestartWebDriverByCase(Boolean.parseBoolean(restartDriverByCase));
        }
    }

    /**
     * get absolute path
     *
     * @param filePath file path
     * @return absolute path
     */
    private String getAbsolutePath(String filePath) {
        return getAbsolutePath(filePath, this.getWorkDir());
    }

    /**
     * get absolute path
     *
     * @param filePath file path
     * @return absolute path
     */
    private String getAbsolutePath(String filePath, String defaultPath) {
        if (filePath != null) {
            if (FileUtil.isAbsolutePath(filePath)) {
                return filePath;
            } else {
                return defaultPath + filePath;
            }
        }
        return StrUtil.EMPTY;
    }

    // user profile path for browser
    private String userProfilePath;
    // web driver name
    private String webDriverName;
    // web driver path
    private String webDriverPath;
    // xlsx file path
    private String testCasePath;
    // test cases to be executed
    private String testCases;
    // file download path
    private String fileDownloadPath;
    // browser width
    private int width;
    // browser height
    private int height;
    // test mode
    private String testMode;
    // cron enabled
    private boolean cronEnabled;
    // cron expression
    private String cronExpression;
    // browser binary path
    private String browserBinaryPath;
    // restart web driver by case
    private boolean restartWebDriverByCase;
    // work dir
    private String workDir;
    // base dir
    private String baseDir;

    public String getUserProfilePath() {
        return userProfilePath;
    }

    public void setUserProfilePath(String userProfilePath) {
        this.userProfilePath = userProfilePath;
    }

    public String getWebDriverName() {
        return webDriverName;
    }

    public void setWebDriverName(String webDriverName) {
        this.webDriverName = webDriverName;
    }

    public String getWebDriverPath() {
        return webDriverPath;
    }

    public void setWebDriverPath(String webDriverPath) {
        this.webDriverPath = webDriverPath;
    }

    public String getTestCasePath() {
        return testCasePath;
    }

    public void setTestCasePath(String testCasePath) {
        this.testCasePath = testCasePath;
    }

    public String getTestCases() {
        return testCases;
    }

    public void setTestCases(String testCases) {
        this.testCases = testCases;
    }

    public String getFileDownloadPath() {
        return fileDownloadPath;
    }

    public void setFileDownloadPath(String fileDownloadPath) {
        this.fileDownloadPath = fileDownloadPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public boolean isCronEnabled() {
        return cronEnabled;
    }

    public void setCronEnabled(boolean cronEnabled) {
        this.cronEnabled = cronEnabled;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getBrowserBinaryPath() {
        return browserBinaryPath;
    }

    public void setBrowserBinaryPath(String browserBinaryPath) {
        this.browserBinaryPath = browserBinaryPath;
    }

    public boolean isRestartWebDriverByCase() {
        return restartWebDriverByCase;
    }

    public void setRestartWebDriverByCase(boolean restartWebDriverByCase) {
        this.restartWebDriverByCase = restartWebDriverByCase;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir.endsWith(File.separator) ? workDir : workDir + File.separator;
    }

    public String getBaseDir() {
        return StrUtil.isEmpty(baseDir) ? SystemUtil.get(AUTO_TEST_HOME) + File.separator : baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir.endsWith(File.separator) ? baseDir : baseDir + File.separator;
    }
}
