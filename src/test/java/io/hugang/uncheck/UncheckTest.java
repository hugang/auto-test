package io.hugang.uncheck;

import io.hugang.util.Utils;
import org.junit.Test;

public class UncheckTest {
    @Test
    public void testUncheckXlsx() {
        Utils.execute("xlsx", "uncheck/uncheck.xlsx");
    }
}
