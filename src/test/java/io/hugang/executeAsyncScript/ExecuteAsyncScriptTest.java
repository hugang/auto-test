package io.hugang.executeAsyncScript;

import io.hugang.util.Utils;
import org.junit.Test;

public class ExecuteAsyncScriptTest {
    @Test
    public void testExecuteAsyncScriptXlsx() {
        Utils.execute("xlsx", "executeAsyncScript/executeAsyncScript.xlsx");
    }
}
