package io.hugang.server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.hugang.BasicExecutor;

import java.io.File;

public class AutoTestServer {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) {
        SimpleServer server = HttpUtil.createServer(9191);
        server.addAction("/excel", (request, response) -> {
            try {
                String path = request.getParam("path");
                String testCases = request.getParam("testcases");
                String mode = request.getParam("mode");
                response.write(JSONUtil.toJsonStr(new BasicExecutor().execute(mode, path, testCases)));
            } catch (Exception ex) {
                log.error(ex);
                response.write("ng");
            }
            response.close();
        }).addAction("/run", (request, response) -> {
            try {
                UploadFile file = request.getMultipart().getFile("file");
                File tempFile = new File(System.currentTimeMillis() + "_" + file.getFileName());
                file.write(tempFile);
                if (file.getFileName().endsWith(".csv") || file.getFileName().endsWith(".json")) {
                    log.info(FileUtil.readString(tempFile, CharsetUtil.defaultCharset()));
                }
                String path = tempFile.getAbsolutePath();
                String testCases = request.getMultipart().getParam("testcases");
                String mode = request.getMultipart().getParam("mode");
                new BasicExecutor().execute(mode, path, testCases);
                response.write(JSONUtil.toJsonStr(new BasicExecutor().execute(mode, path, testCases)));
            } catch (Exception ex) {
                log.error(ex);
                response.write("ng");
            }
            response.close();
        });
        server.start();
    }
}
