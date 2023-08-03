package io.hugang.util;

import io.hugang.BasicExecutor;
import io.hugang.execute.CommandExecuteUtil;

import java.util.Objects;

public class Utils {
    public static void execute(String mode, String path) {
        new BasicExecutor().execute(mode, Objects.requireNonNull(Utils.class.getClassLoader().getResource(path)).getPath());
        System.out.println(CommandExecuteUtil.getVariables());
    }

    public static void execute(String mode, String path, String cases) {
        new BasicExecutor().execute(mode, path, cases);
        System.out.println(CommandExecuteUtil.getVariables());
    }
}
