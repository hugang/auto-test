package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;

import java.nio.charset.Charset;

public class CallApiCommand extends Command {
    private static final Log log = Log.get();

    public CallApiCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public String getCommand() {
        return "callApi";
    }

    @Override
    public boolean _execute() {
        if (ObjectUtil.isEmpty(getValue())) {
            return true;
        }
        // get url from target
        String url = render(getTarget());
        // get other options from value
        String options = render(this.getDictStr("value", getValue()));
        JSONObject obj;
        if (options.contains(".json")) {
            options = this.getFilePath(options);
            // get json string from file
            options = render(FileUtil.readString(options, Charset.defaultCharset()));
        }
        // parse options to json
        obj = JSONUtil.parseObj(options);
        // get method
        String methodStr = obj.getStr("method");
        Method method = Method.valueOf(methodStr);
        // get params
        String params = obj.getStr("params");
        // if params is null, return true
        if (ObjectUtil.isNotEmpty(params)) {
            // join params to url, params is a string like "name=xxx&age=xxx"
            url = url.concat("?").concat(params);
        }
        // get headers
        Object headers = obj.get("headers");

        // get body
        String body = obj.getStr("body");
        // get proxy
        String proxy = obj.getStr("proxy");

        HttpRequest httpRequest = HttpRequest.of(url).method(method);
        // if method is not get and body is not null, set body to request
        if (!"GET".equals(methodStr) && StrUtil.isNotEmpty(body)) {
            // set body to request
            httpRequest.body(body);
        }
        // loop proxy and get proxy host and port by key
        if (proxy != null) {
            JSONObject proxyObj = JSONUtil.parseObj(proxy);
            String proxyHost = proxyObj.getStr("host");
            int proxyPort = proxyObj.getInt("port");
            httpRequest.setHttpProxy(proxyHost, proxyPort);
            String proxyUser = proxyObj.getStr("username");
            String proxyPass = proxyObj.getStr("password");
            if (StrUtil.isNotEmpty(proxyUser) && StrUtil.isNotEmpty(proxyPass)) {
                httpRequest.basicProxyAuth(proxyUser, proxyPass);
            }
        } else if (this.getAutoTestConfig().getProxyHost() != null && this.getAutoTestConfig().getProxyPort() != 0) {
            httpRequest.setHttpProxy(this.getAutoTestConfig().getProxyHost(), this.getAutoTestConfig().getProxyPort());
            // set proxy username and password
            if (StrUtil.isNotEmpty(this.getAutoTestConfig().getProxyUser()) && StrUtil.isNotEmpty(this.getAutoTestConfig().getProxyPassword())) {
                httpRequest.basicProxyAuth(this.getAutoTestConfig().getProxyUser(), this.getAutoTestConfig().getProxyPassword());
            }
        }

        // if headers is null, return true
        if (headers != null) {
            // set headers to request, headers is a json object like {"Content-Type": "application/json"}
            // loop headers and get key and value
            JSONObject headerObject = (JSONObject) headers;
            // loop headers and set header to request
            for (String key : headerObject.keySet()) {
                String value = headerObject.getStr(key);
                httpRequest.header(key, value);
            }
        }
        try {
            JSONArray store;
            JSONObject jsonObject;
            try (HttpResponse response = httpRequest.execute()) {
                log.info("response: {}", response.body());
                Object o = obj.get("store");
                if (o == null) {
                    return true;
                }
                // the object o is a json array, so we need to loop it
                store = (JSONArray) o;
                // get value from response body
                jsonObject = JSONUtil.parseObj(response.body());
                for (int i = 0; i < store.size(); i++) {
                    JSONObject storeObj = store.getJSONObject(i);
                    String name = storeObj.getStr("name");
                    String value = storeObj.getStr("value");
                    String responseKey = storeObj.getStr("responseKey");
                    if (responseKey != null) {
                        Object byPath = JSONUtil.getByPath(jsonObject, responseKey);
                        String responseValue = "";
                        if (ObjectUtil.isNotEmpty(byPath)) {
                            responseValue = byPath.toString();
                        }
                        // store value to context
                        this.setVariable(name, responseValue);
                    } else {
                        // store value to context
                        this.setVariable(name, value);
                    }
                }
                // generate result detail
                this.appendDict("requestUrl", url);
                this.appendDict("requestHeader", headers);
                this.appendDict("requestBody", body);
                this.appendDict("responseBody", response.body());
            }
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
