package io.hugang.waitForElementPresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class WaitForElementPresentTest {
    @Test
    public void testWaitForElementPresentXlsx() {
        Utils.execute("xlsx", "waitForElementPresent/waitForElementPresent.xlsx");
    }
}
