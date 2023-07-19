package io.hugang.execute.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import io.hugang.execute.CommandExecuteUtil;

public class CallApiCommand extends Command {
    public CallApiCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        // get url from target
        String url = getTarget();
        // get other options from value
        String options = getValue();
        // parse options to json
        JSONObject obj = JSONUtil.parseObj(options);
        // get method
        String methodStr = obj.getStr("method");
        Method method = Method.valueOf(methodStr);
        // get params
        String params = obj.getStr("params");
        // if params is null, return true
        if (params != null) {
            // join params to url, params is a string like "name=xxx&age=xxx"
            url = url.concat("?").concat(params);
        }
        // get headers
        Object headers = obj.get("headers");

        // get body
        String body = obj.getStr("body");
        // get proxy
        String proxy = obj.getStr("proxy");

        HttpRequest httpRequest = HttpRequest.of(url)
                .method(method)
                .body(body);
        // loop proxy and get proxy host and port by key
        if (proxy != null) {
            JSONObject proxyObj = JSONUtil.parseObj(proxy);
            String proxyHost = proxyObj.getStr("host");
            int proxyPort = proxyObj.getInt("port");
            httpRequest.setHttpProxy(proxyHost, proxyPort);
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
            HttpResponse response = httpRequest.execute();
            Object o = obj.get("store");
            if (o == null) {
                return true;
            }
            // the object o is a json array, so we need to loop it
            JSONArray store = (JSONArray) o;
            for (int i = 0; i < store.size(); i++) {
                JSONObject storeObj = store.getJSONObject(i);
                String name = storeObj.getStr("name");
                String value = storeObj.getStr("value");
                String responseKey = storeObj.getStr("responseKey");
                if (responseKey != null) {
                    // get value from response body
                    String responseValue = JSONUtil.parseObj(response.body()).getStr(responseKey);
                    // store value to context
                    CommandExecuteUtil.setVariable(name, responseValue);
                } else {
                    // store value to context
                    CommandExecuteUtil.setVariable(name, value);
                }
            }
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
