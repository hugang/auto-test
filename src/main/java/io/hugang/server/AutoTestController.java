package io.hugang.server;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import io.hugang.BasicExecutor;
import io.hugang.execute.Commands;
import io.hugang.util.ThreadContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AutoTestController {
    private static final Log log = Log.get();

    @RequestMapping("/test")
    public void test() {
        System.out.println("test");
    }

    @RequestMapping("/local")
    public Map<String, Object> local(@RequestParam String path, @RequestParam String testcases, @RequestParam(required = false) String mode) {
        mode = mode == null ? "xlsx" : mode;
        List<Commands> result = new BasicExecutor().execute(mode, path, testcases);
        return responseResult(result);
    }

    @RequestMapping("/remote")
    public Map<String, Object> remote(@RequestParam MultipartFile file, @RequestParam String testcases) throws IOException {
        File tempFile = FileUtil.file(ThreadContext.getAutoTestConfig().getFileDownloadPath().concat("/").concat(System.currentTimeMillis() + "_" + file.getOriginalFilename()));
        // save multipart file to local tempFile
        file.transferTo(tempFile);
        String fileType = FileTypeUtil.getType(tempFile);
        log.info("file type: {}", fileType);
        // only support xlsx, csv and json file
        if (!("zip".equals(fileType) && tempFile.getName().endsWith("xlsm"))
                && !"xlsx".equals(fileType) && !"csv".equals(fileType) && !"json".equals(fileType)) {
            log.error("not support file type: " + fileType);
            return null;
        }
        String path = tempFile.getAbsolutePath();
        String mode;
        if ("json".equals(fileType)) {
            mode = "json";
        } else if ("csv".equals(fileType)) {
            mode = "csv";
        } else {
            mode = "xlsx";
        }
        List<Commands> result = new BasicExecutor().execute(mode, path, testcases);
        return responseResult(result);
    }

    @RequestMapping("/flow")
    public Map<String, Object> flow(@RequestBody String jsonFlow) {
        String path = ThreadContext.getAutoTestConfig().getFileDownloadPath().concat("/").concat(System.currentTimeMillis() + ".json");
        FileUtil.writeString(jsonFlow, path, "utf-8");
        List<Commands> result = new BasicExecutor().execute("json", path);
        return responseResult(result);
    }

    private Map<String, Object> responseResult(List<Commands> result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("commands", result);
        resultMap.put("variables", ThreadContext.getVariables());
        return resultMap;
    }

    public static class RequestVO {
        private String path;
        private String testcases;
        private String mode;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTestcases() {
            return testcases;
        }

        public void setTestcases(String testcases) {
            this.testcases = testcases;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
