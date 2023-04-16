package io.hugang.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;

import java.io.File;

public class AutoTestConfig {
    public static final String WORK_DIR = new File("").getAbsolutePath();

    /**
     * read user properties
     */
    public void readUserProperties() {
        // read user.properties
        File file = FileUtil.file(WORK_DIR + "/conf/user.properties");
        if (!file.exists()) {
            return;
        }
        Setting setting = SettingUtil.get(file.getAbsolutePath());
        String userProfilePath = setting.get("user.profile.path");
        if (userProfilePath != null) {
            this.setUserProfilePath(userProfilePath);
        }
        String webDriverName = setting.get("web.driver.name");
        if (webDriverName != null) {
            this.setWebDriverName(webDriverName);
        }
        String webDriverPath = setting.get("web.driver.path");
        if (webDriverPath != null) {
            this.setWebDriverPath(webDriverPath);
        }
        String csvFilePath = setting.get("csv.file.path");
        if (csvFilePath != null) {
            this.setCsvFilePath(csvFilePath);
        }
        String xlsxFilePath = setting.get("xlsx.file.path");
        if (xlsxFilePath != null) {
            this.setXlsxFilePath(xlsxFilePath);
        }
        String sideFilePath = setting.get("side.file.path");
        if (sideFilePath != null) {
            this.setSideFilePath(sideFilePath);
        }
        String fileDownloadPath = setting.get("file.download.path");
        if (fileDownloadPath != null) {
            this.setFileDownloadPath(fileDownloadPath);
        }
        String width = setting.get("width");
        if (width != null) {
            this.setWidth(Integer.parseInt(width));
        }
        String height = setting.get("height");
        if (height != null) {
            this.setHeight(Integer.parseInt(height));
        }
        String inputMedia = setting.get("input.media");
        if (inputMedia != null) {
            this.setInputMedia(inputMedia);
        }
    }

    // user profile path for browser
    private String userProfilePath;
    // web driver name
    private String webDriverName;
    // web driver path
    private String webDriverPath;
    // csv file path
    private String csvFilePath;
    // xlsx file path
    private String xlsxFilePath;
    // test cases to be executed
    private String testCases;
    // file download path
    private String fileDownloadPath;
    // browser width
    private int width;
    // browser height
    private int height;
    // input media
    private String inputMedia;
    // side file path
    private String sideFilePath;


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

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public String getXlsxFilePath() {
        return xlsxFilePath;
    }

    public void setXlsxFilePath(String xlsxFilePath) {
        this.xlsxFilePath = xlsxFilePath;
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

    public String getInputMedia() {
        return inputMedia;
    }

    public void setInputMedia(String inputMedia) {
        this.inputMedia = inputMedia;
    }

    public String getSideFilePath() {
        return sideFilePath;
    }

    public void setSideFilePath(String sideFilePath) {
        this.sideFilePath = sideFilePath;
    }
}
