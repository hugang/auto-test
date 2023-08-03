package io.hugang.bean;

import com.beust.jcommander.Parameter;

public class AutoTestCommandLineOption {

    @Parameter(names = {"-f", "--file"}, description = "the test case file to be executed, specific absolute path,  if relative path, it will look for the file in the work directory.")
    private String filePath;
    @Parameter(names = {"-c", "--cases"}, description = "the test cases to be executed, split by comma. eg. 1-2,13")
    private String cases;
    @Parameter(names = {"-m", "--mode"}, description = "test mode, default is xlsx, optional: xlsx, csv, json.")
    private String mode;
    @Parameter(names = {"-d", "--workDir"}, description = "work directory to store test case file and test result file.")
    private String workDir;
    @Parameter(names = {"-b", "--baseDir"}, description = "base directory of the program.")
    private String baseDir;
    @Parameter(names = {"-h", "--help"}, description = "help message", help = true)
    private boolean help;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
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

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    @Override
    public String toString() {
        return "AutoTestCommandLineOption{" +
                "filePath='" + filePath + '\'' +
                ", cases='" + cases + '\'' +
                ", mode='" + mode + '\'' +
                ", workDir='" + workDir + '\'' +
                ", baseDir='" + baseDir + '\'' +
                ", help=" + help +
                '}';
    }
}
