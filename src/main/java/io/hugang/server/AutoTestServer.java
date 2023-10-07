package io.hugang.server;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.BasicExecutor;

public class AutoTestServer {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) {
        SimpleServer server = HttpUtil.createServer(9191);
        server.addAction("/run", (request, response) -> {
            try {
                String path = request.getParam("path");
                String testCases = request.getParam("testCases");
                new BasicExecutor().execute("xlsx", path, testCases);
                response.write("ok");
            } catch (Exception ex) {
                log.error(ex);
            }
        });
        server.start();
    }
}
