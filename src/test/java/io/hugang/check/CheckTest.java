package io.hugang.check;

import io.hugang.util.Utils;
import org.junit.Test;

public class CheckTest {
    @Test
    public void testCheckXlsx() {
        Utils.execute("xlsx", "check/check.xlsx");
    }
}
