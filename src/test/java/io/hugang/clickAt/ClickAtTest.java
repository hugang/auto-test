package io.hugang.clickAt;

import io.hugang.util.Utils;
import org.junit.Test;

public class ClickAtTest {
    @Test
    public void testClickAtExist() {
        Utils.execute("xlsx", "clickAt/clickAt.xlsx");
    }
}
