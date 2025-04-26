package io.hugang.bean;

import cn.hutool.json.JSONUtil;
import org.openqa.selenium.devtools.v135.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v135.network.model.ResponseReceived;

import java.util.HashMap;

public class NetworkAccessLog {
    String  requestId;
    RequestWillBeSent requestWillBeSent;
    ResponseReceived responseReceived;
    String responseBody;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestWillBeSent getRequestWillBeSent() {
        return requestWillBeSent;
    }

    public void setRequestWillBeSent(RequestWillBeSent requestWillBeSent) {
        this.requestWillBeSent = requestWillBeSent;
    }

    public ResponseReceived getResponseReceived() {
        return responseReceived;
    }

    public void setResponseReceived(ResponseReceived responseReceived) {
        this.responseReceived = responseReceived;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        HashMap<String, Object> accessMap = new HashMap<>();
        accessMap.put("requestId", requestId);
        accessMap.put("request", requestWillBeSent.getRequest());
        accessMap.put("response", responseReceived.getResponse());
        accessMap.put("responseBody", responseBody);
        return JSONUtil.toJsonStr(accessMap);
    }
}
