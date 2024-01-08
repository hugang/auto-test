package io.hugang.waitForElementEditable;

import io.hugang.util.Utils;
import org.junit.Test;

public class WaitForElementEditableTest {
    @Test
    public void testWaitForElementEditableXlsx() {
        Utils.execute("xlsx", "waitForElementEditable/waitForElementEditable.xlsx");
    }
}
