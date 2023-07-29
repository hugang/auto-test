package io.hugang.doubleClickAt;

import io.hugang.util.Utils;
import org.junit.Test;

public class DoubleClickAtTest {
    @Test
    public void testDoubleClickAtExist() {
        Utils.execute("xlsx", "doubleClickAt/doubleClickAt.xlsx");
    }
}
