package io.hugang.waitForElementVisible;

import io.hugang.util.Utils;
import org.junit.Test;

public class WaitForElementVisibleTest {
    @Test
    public void testWaitForElementVisibleXlsx() {
        Utils.execute("xlsx", "waitForElementVisible/waitForElementVisible.xlsx");
    }
}
