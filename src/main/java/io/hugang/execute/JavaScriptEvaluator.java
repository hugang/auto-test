package io.hugang.execute;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

public class JavaScriptEvaluator {
    public static Object evaluate(String expression, Map<String, ?> variables) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, ?> entry : variables.entrySet()) {
                engine.put(entry.getKey(), entry.getValue());
            }
        }
        return engine.eval(expression);
    }
}
