package io.hugang.execute.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

/**
 * jenkins job command executor
 * <p>
 *
 * @author hugang
 */
public class JenkinsJobCommand extends Command {
    // log
    private static final Log log = LogFactory.get();

    public JenkinsJobCommand() {
    }

    public JenkinsJobCommand(String command, String target) {
        super(command, target);
    }

    public JenkinsJobCommand(String command, String target, String value) {
        super(command, target, value);
    }

    public JenkinsJobCommand(String command, String description, String target, String value) {
        super(command, description, target, value);
    }

    /**
     * execute jenkins job command
     *
     * @return success or not
     */
    @Override
    public boolean execute() {
        String jobUrl = this.getTarget();
        String parameters = render(this.getValue());
        JSONObject parametersJson = JSONUtil.parseObj(parameters);

        String userName = (String) parametersJson.get("userName");
        String token = (String) parametersJson.get("token");
        JSONObject parameterObj = (JSONObject) parametersJson.get("parameters");

        if (!parameterObj.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(jobUrl)
                    .append("/buildWithParameters?");
            // append parameters in parameterObj
            parameterObj.forEach((key, value) -> sb.append(key)
                    .append("=")
                    .append(value)
                    .append("&"));
            jobUrl = sb.substring(0, sb.toString().length() - 1);
        } else {
            jobUrl = jobUrl + "/build";
        }
        log.info("job url: {}", jobUrl);
        return this.executeJob(jobUrl, userName, token) == 1;
    }

    /**
     * wait for execution id
     *
     * @param jobUrl   jenkins job url
     * @param userName jenkins user name
     * @param token    jenkins token
     * @return execution id
     */
    private Integer executeJob(String jobUrl, String userName, String token) {
        try {
            HttpRequest httpRequest = HttpUtil.createPost(jobUrl).basicAuth(userName, token);
            String location;
            try (HttpResponse response = httpRequest.execute()) {
                location = response.header("location");
            }
            log.info("sequence url : {}", location);
            String executionUrl = this.waitForExecutionUrl(location, userName, token);
            log.info("execution url : {}", executionUrl);
            String result = this.waitForExecutionFinished(executionUrl, userName, token);

            if ("SUCCESS".equals(result)) {
                return 1;
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * wait for execution finished
     *
     * @param executionUrl jenkins execution url
     * @param userName     jenkins user name
     * @param token        jenkins token
     * @return execution result, success or failure
     */
    private String waitForExecutionFinished(String executionUrl, String userName, String token) {
        do {
            try {
                JSONObject jsonObject;
                HttpRequest httpRequest = HttpUtil.createGet(executionUrl + "api/json").basicAuth(userName, token);
                try (HttpResponse response = httpRequest.execute()) {
                    jsonObject = JSONUtil.parseObj(response.body());
                }
                Object executable = jsonObject.getByPath("result");
                if (executable != null) {
                    String result = executable.toString();
                    if ("SUCCESS".equals(result) || "FAILURE".equals(result)) {
                        log.info("execution finished, result: {}", result);
                        return result;
                    }
                }
                log.info("wait for execution finished");
                // wait for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    /**
     * wait for execution url
     *
     * @param sequencedUrl jenkins sequenced url
     * @param userName     jenkins user name
     * @param token        jenkins token
     * @return execution id
     */
    private String waitForExecutionUrl(String sequencedUrl, String userName, String token) {
        do {
            try {
                JSONObject jsonObject;
                HttpRequest httpRequest = HttpUtil.createGet(sequencedUrl + "api/json").basicAuth(userName, token);
                try (HttpResponse response = httpRequest.execute()) {
                    jsonObject = JSONUtil.parseObj(response.body());
                }
                Object executable = jsonObject.get("executable");
                if (executable != null) {
                    JSONObject executableJson = ((JSONObject) executable);
                    String url = (String) executableJson.get("url");
                    if (url != null) {
                        return url;
                    }
                }
                log.info("wait for execution url");
                // wait for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }
}
