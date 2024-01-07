package io.hugang.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExecutionResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mode;
    private String path;
    private String cases;
    private String result;
    private String message;
    private List<ExecutionCaseResultDetail> details;

    public String getMode() {
        return mode;
    }

    public String getPath() {
        return path;
    }

    public String getCases() {
        return cases;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExecutionCaseResultDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExecutionCaseResultDetail> details) {
        this.details = details;
    }

    public void appendCaseResultDetail(List<ExecutionCommandResultDetail> commands) {
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(new ExecutionCaseResultDetail(commands, ""));
    }

}
