package io.hugang.server;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import io.hugang.BasicExecutor;
import io.hugang.execute.Commands;
import io.hugang.util.ThreadContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoTestServer {
    private static final Log log = Log.get();

    private static void setCorsHeaders(cn.hutool.http.server.HttpServerResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void main(String[] args) {
        SimpleServer server = HttpUtil.createServer(9191);
        server.addAction("/", (request, response) -> {
                    setCorsHeaders(response);
                    response.setContentType("text/html; charset=utf-8");
                    response.write("""
                            <style> form {width: 300px;margin: 0 auto;}  label {display: block;margin-bottom: 10px;color: #333;font-weight: bold;}  input[type="text"], input[type="file"] {width: 100%;padding: 10px;border: 1px solid #ccc;border-radius: 4px;box-sizing: border-box;margin-bottom: 20px;}  input[type="submit"] {background-color: #4CAF50;color: white;padding: 10px 20px;border: none;border-radius: 4px;cursor: pointer;}  input[type="submit"]:hover {background-color: #45a049;}</style>
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
                    setCorsHeaders(response);
                    try {
                        String path = request.getParam("path");
                        String testCases = request.getParam("testcases");
                        String mode = request.getParam("mode");
                        mode = mode == null ? "xlsx" : mode;
                        List<Commands> result = new BasicExecutor().execute(mode, path, testCases);
                        responseResult(response, result);
                    } catch (Exception ex) {
                        log.error(ex);
                        response.write("ng");
                    }
                    response.close();
                })
                // run test case from remote pc, upload test case xlsx
                .addAction("/remote", (request, response) -> {
                    setCorsHeaders(response);
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
                        List<Commands> result = new BasicExecutor().execute(mode, path, testCases);
                        responseResult(response, result);
                    } catch (Exception ex) {
                        log.error(ex);
                        response.write("ng");
                    }
                    response.close();
                })
                .addAction("/flow", (request, response) -> {
                    setCorsHeaders(response);
                    try {
                        // if not post method, return 204
                        if (!"POST".equalsIgnoreCase(request.getMethod())) {
                            response.sendOk();
                            response.close();
                            return;
                        }
                        // parse request body
                        String body = request.getBody();
                        System.out.println(body);
                        // save body to json file
                        String path = ThreadContext.getAutoTestConfig().getBaseDir() + System.currentTimeMillis() + ".json";
                        FileUtil.writeString(body, path, "utf-8");
                        List<Commands> result = new BasicExecutor().execute("json", path);
                        responseResult(response, result);
                    } catch (Exception ex) {
                        log.error(ex);
                        response.write("ng");
                    }
                })

                .start();
        System.out.println("server started at http://localhost:9191/");
    }

    private static void responseResult(HttpServerResponse response, List<Commands> result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("commands", result);
        resultMap.put("variables", ThreadContext.getVariables());
        response.setContentType("application/json; charset=utf-8");
        log.info(JSONUtil.toJsonPrettyStr(resultMap));
        response.write(JSONUtil.toJsonPrettyStr(resultMap));
    }
}
