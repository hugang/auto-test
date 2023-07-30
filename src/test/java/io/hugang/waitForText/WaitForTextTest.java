package io.hugang.waitForText;

import io.hugang.util.Utils;
import org.junit.Test;

public class WaitForTextTest {
    @Test
    public void testWaitForTextXlsx() {
        Utils.execute("xlsx", "waitForText/waitForText.xlsx");
    }
}
