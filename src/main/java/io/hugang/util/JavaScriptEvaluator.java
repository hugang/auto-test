package io.hugang.util;

import cn.hutool.script.ScriptUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class JavaScriptEvaluator {
    public static Object evaluate(String expression, Map<String, ?> variables) throws ScriptException {
        ScriptEngine engine = ScriptUtil.getJsEngine();
        // inject console.log function for debugging
        String consoleDef = "var console = { log: print, error: print, warn: print, info: print };";
        engine.eval(consoleDef);
        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, ?> entry : variables.entrySet()) {
                engine.put(entry.getKey(), entry.getValue());
            }
        }
        return engine.eval(expression);
    }
}
