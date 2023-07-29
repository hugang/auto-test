package io.hugang.runCase;

import io.hugang.util.Utils;
import org.junit.Test;

public class RunCaseTest {
    @Test
    public void testRunCaseXlsx() {
        Utils.execute("xlsx", "runCase/runCase.xlsx");
    }
}
