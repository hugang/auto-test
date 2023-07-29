package io.hugang.executeScript;

import io.hugang.util.Utils;
import org.junit.Test;

public class ExecuteScriptTest {
    @Test
    public void testExecuteScriptXlsx() {
        Utils.execute("xlsx", "executeScript/executeScript.xlsx");
    }
}
