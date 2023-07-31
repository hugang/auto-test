package io.hugang;

import io.hugang.util.Utils;
import org.junit.Test;

public class AllTest {
    @Test
    public void testClickXlsx() {
        Utils.execute("xlsx", "testcase1.xlsx");
    }

    @Test
    public void testTimesXlsx() {
        Utils.execute("xlsx", "testcase2.xlsx");
    }

    @Test
    public void testNewXlsx() {
        Utils.execute("xlsx", "testcase3.xlsx", "1-2");
    }
}
