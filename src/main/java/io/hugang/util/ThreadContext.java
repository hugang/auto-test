package io.hugang.util;


import cn.hutool.core.lang.Dict;
import io.hugang.config.AutoTestConfig;

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
        return variables.get();
    }

    public static AutoTestConfig getAutoTestConfig() {
        AutoTestConfig autoTestConfig = (AutoTestConfig) ThreadContext.get("autoTestConfig");
        if (autoTestConfig == null) {
            autoTestConfig = new AutoTestConfig();
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

}
