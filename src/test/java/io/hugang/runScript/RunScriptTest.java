package io.hugang.runScript;

import io.hugang.execute.CommandExecuteUtil;
import io.hugang.util.Utils;
import org.junit.Test;

public class RunScriptTest {
    @Test
    public void testRunScriptXlsx() {
        Utils.execute("xlsx", "runScript/runScript.xlsx");
    }
}
