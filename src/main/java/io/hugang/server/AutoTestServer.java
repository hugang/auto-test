package io.hugang.server;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.BasicExecutor;

import java.io.File;

public class AutoTestServer {
    private static final Log log = Log.get();

    public static void main(String[] args) {
        SimpleServer server = HttpUtil.createServer(9191);
        server.addAction("/", (request, response) -> {
                    response.setContentType("text/html; charset=utf-8");
                    response.write("""
                            <form method="post" action="/remote" enctype="multipart/form-data">
                                <label for="file">file</label><input type="file" name="file" id="file"><br>
                                <label for="testcases">testcases</label><input type="text" name="testcases" id="testcases"><br>
                                <input type="submit" value="submit"><br>
                            </form>
                            """);
                    response.close();
                })
                // run test case from local pc, the test case store in the work folder
                // e.g. http://localhost:9191/local?testcases=1&path=src/test/resources/recorder/recorder.xlsx
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
                        String fileType = FileTypeUtil.getType(tempFile);
                        log.info("file type: {}", fileType);
                        // only support xlsx, csv and json file
                        if (!("zip".equals(fileType) && tempFile.getName().endsWith("xlsm"))
                                && !"xlsx".equals(fileType) && !"csv".equals(fileType) && !"json".equals(fileType)) {
                            response.write("not support file type: " + fileType);
                            response.close();
                            return;
                        }
                        String path = tempFile.getAbsolutePath();
                        String testCases = request.getMultipart().getParam("testcases");
                        String mode;
                        if ("json".equals(fileType)) {
                            mode = "json";
                        } else if ("csv".equals(fileType)) {
                            mode = "csv";
                        } else {
                            mode = "xlsx";
                        }
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
