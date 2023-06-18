package io.hugang.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;

import java.io.File;

/**
 * auto test config
 *
 * @author hugang
 */
public class AutoTestConfig {
    // the working path
    public static final String WORK_DIR = new File("").getAbsolutePath();
    // config group of browser
    public static final String GROUP_BROWSER = "browser";
    // config group of test case
    public static final String GROUP_TEST_CASE = "test-case";
    // config group of base
    public static final String GROUP_BASE = "base";

    /**
     * read auto-test.conf
     */
    public void readConfigurations() {
        // read auto-test.conf
        File file = FileUtil.file(WORK_DIR + "/conf/auto-test.conf");
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
        this.setWebDriverPath(getAbsolutePath(webDriverPath));

        String testCasePath = setting.get(GROUP_TEST_CASE, "test.case.path");
        this.setTestCasePath(getAbsolutePath(testCasePath));

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
        String inputMedia = setting.get(GROUP_TEST_CASE, "test.case.mode");
        if (inputMedia != null) {
            this.setTestMode(inputMedia);
        }
        String testCases = setting.get(GROUP_TEST_CASE, "xlsx.specific.testcases");
        if (testCases != null) {
            this.setTestCases(testCases);
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
    }

    /**
     * get absolute path
     *
     * @param filePath file path
     * @return absolute path
     */
    private String getAbsolutePath(String filePath) {
        if (filePath != null) {
            if (FileUtil.isAbsolutePath(filePath)) {
                return filePath;
            } else {
                return WORK_DIR + File.separator + filePath;
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
}
