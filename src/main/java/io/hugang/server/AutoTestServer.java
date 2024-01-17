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
        server
                // run test case from local pc, the test case store in the work folder
                // eg. http://localhost:9191/local?testcases=1&path=src/test/resources/recorder/recorder.xlsx
                .addAction("/local", (request, response) -> {
                    try {
                        String path = request.getParam("path");
                        String testCases = request.getParam("testcases");
                        String mode = request.getParam("mode");
                        mode = mode == null ? "xlsx" : mode;
                        response.write(JSONUtil.toJsonStr(new BasicExecutor().execute(mode, path, testCases)));
                    } catch (Exception ex) {
                        log.error(ex);
                        response.write("ng");
                    }
                    response.close();
                })
                // run test case from remote pc, upload test case xlsx
                .addAction("/remote", (request, response) -> {
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
                        mode = mode == null ? "xlsx" : mode;
                        response.write(JSONUtil.toJsonStr(new BasicExecutor().execute(mode, path, testCases)));
                    } catch (Exception ex) {
                        log.error(ex);
                        response.write("ng");
                    }
                    response.close();
                })
                .start();
    }
}
