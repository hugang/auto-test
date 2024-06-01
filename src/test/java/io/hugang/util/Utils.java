package io.hugang.util;

import cn.hutool.json.JSONUtil;
import io.hugang.BasicExecutor;
import io.hugang.execute.Commands;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static void execute(String mode, String path) {
        List<Commands> execute = BasicExecutor.create().execute(mode, Objects.requireNonNull(Utils.class.getClassLoader().getResource(path)).getPath());
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    public static void execute(String mode, String path, String cases) {
        List<Commands> execute = BasicExecutor.create().execute(mode, path, cases);
        System.out.println(JSONUtil.toJsonStr(execute));
    }
}
