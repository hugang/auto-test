package io.hugang.util;

import cn.hutool.json.JSONUtil;
import io.hugang.BasicExecutor;
import io.hugang.bean.ExecutionResult;

import java.util.Objects;

public class Utils {
    public static void execute(String mode, String path) {
        ExecutionResult execute = new BasicExecutor().execute(mode, Objects.requireNonNull(Utils.class.getClassLoader().getResource(path)).getPath());
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    public static void execute(String mode, String path, String cases) {
        ExecutionResult execute = new BasicExecutor().execute(mode, path, cases);
        System.out.println(JSONUtil.toJsonStr(execute));
    }
}
