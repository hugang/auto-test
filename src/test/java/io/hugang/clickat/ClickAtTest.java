package io.hugang.clickat;

import io.hugang.util.Utils;
import org.junit.Test;

public class ClickAtTest {
    @Test
    public void testClickAtExist() {
        Utils.execute("xlsx", "clickat/clickAt.xlsx");
    }
}
