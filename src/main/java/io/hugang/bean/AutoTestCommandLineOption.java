package io.hugang.bean;

import com.beust.jcommander.Parameter;

public class AutoTestCommandLineOption {

    @Parameter(names = {"-f", "--help"}, description = "test case file path", help = true)
    private String path;
    @Parameter(names = {"-t", "--testcases"}, description = "test cases", help = true)
    private String testCases;
    @Parameter(names = {"-m", "--mode"}, description = "test mode", help = true)
    private String mode;
    @Parameter(names = {"-d", "--workDir"}, description = "work directory", help = true)
    private String workDir;
    @Parameter(names = {"-c", "--currentDir"}, description = "current directory", help = true)
    private String currentDir;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTestCases() {
        return testCases;
    }

    public void setTestCases(String testCases) {
        this.testCases = testCases;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    @Override
    public String toString() {
        return "AutoTestCommandLineOption{" +
                "path='" + path + '\'' +
                ", testCases='" + testCases + '\'' +
                ", mode='" + mode + '\'' +
                ", workDir='" + workDir + '\'' +
                ", currentDir='" + currentDir + '\'' +
                '}';
    }
}
