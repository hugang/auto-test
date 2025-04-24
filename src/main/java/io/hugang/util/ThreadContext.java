package io.hugang.util;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import io.hugang.RunAutoTest;
import io.hugang.config.AutoTestConfig;

import java.util.Date;
import java.util.UUID;

public class ThreadContext {

    private static final ThreadLocal<Dict> variables = new ThreadLocal<>();

    public static void put(String key, Object value) {
        Dict dict = variables.get();
        if (dict == null) {
            dict = new Dict();
            ThreadContext.variables.set(dict);
        }
        dict.set(key, value);
    }

    public static Object get(String key) {
        Dict dict = variables.get();
        if (dict == null) {
            return null;
        }
        return dict.get(key);
    }

    public static String getStr(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public static void remove(String key) {
        Dict dict = variables.get();
        if (dict != null) {
            dict.remove(key);
        }
    }

    public static boolean containsKey(String key) {
        Dict dict = variables.get();
        if (dict == null) {
            return false;
        }
        return dict.containsKey(key);
    }

    public static void removeAll() {
        variables.remove();
    }

    public static Dict getVariables() {
        Dict dict = variables.get();
        if (dict == null) {
            dict = new Dict();
            ThreadContext.variables.set(dict);
        }
        return variables.get();
    }

    public static AutoTestConfig getAutoTestConfig() {
        // 如果从命令行启动,则直接返回命令行中初始化的配置
        if (RunAutoTest.AUTO_TEST_CONFIG.getInitialized()){
            return RunAutoTest.AUTO_TEST_CONFIG;
        }
        // 如果通过API调用,则从ThreadContext中获取配置
        AutoTestConfig autoTestConfig = (AutoTestConfig) ThreadContext.get("autoTestConfig");
        if (autoTestConfig == null) {
            autoTestConfig = new AutoTestConfig();
            autoTestConfig.readConfigurations();
            ThreadContext.put("autoTestConfig", autoTestConfig);
        }
        return autoTestConfig;
    }
    public static void setAutoTestConfig(AutoTestConfig config) {
        ThreadContext.put("autoTestConfig", config);
    }

    public static Dict getVariableMap() {
        return ThreadContext.getVariables();
    }

    public static String getTestCasePath() {
        return (String) ThreadContext.get("testCasePath");
    }
    public static void setTestCasePath(String path) {
        ThreadContext.put("testCasePath", path);
    }

    public static String getTestMode() {
        return (String) ThreadContext.get("testMode");
    }
    public static void setTestMode(String mode) {
        ThreadContext.put("testMode", mode);
    }
    public static String getTestCases() {
        return (String) ThreadContext.get("testCases");
    }
    public static void setTestCases(String cases) {
        ThreadContext.put("testCases", cases);
    }
    public static String getReportPath() {
        return (String) ThreadContext.get("reportPath");
    }
    public static void setReportPath(String path) {
        ThreadContext.put("reportPath", path);
    }
    public static String getReportUuid() {
        if (StrUtil.isEmpty((String) ThreadContext.get("__uuid__"))){
            ThreadContext.put("__uuid__", DatePattern.PURE_DATETIME_MS_FORMAT.format(new Date()).concat("_").concat(UUID.randomUUID().toString()));
        }
        return (String) ThreadContext.get("__uuid__");
    }
    public static void setIsWebCommand() {
        ThreadContext.put("__isWebCommand__", "__isWebCommand__");
    }
}
