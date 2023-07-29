package io.hugang.run;

import io.hugang.util.Utils;
import org.junit.Test;

public class RunTest {
    @Test
    public void testRunXlsx() {
        Utils.execute("xlsx", "run/run.xlsx");
    }
}
