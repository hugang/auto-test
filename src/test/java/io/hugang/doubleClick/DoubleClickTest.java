package io.hugang.doubleClick;

import io.hugang.util.Utils;
import org.junit.Test;

public class DoubleClickTest {
    @Test
    public void testDoubleClickExist() {
        Utils.execute("xlsx", "doubleClick/doubleClick.xlsx");
    }
}
