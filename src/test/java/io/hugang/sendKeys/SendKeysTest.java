package io.hugang.sendKeys;

import io.hugang.util.Utils;
import org.junit.Test;

public class SendKeysTest {
    @Test
    public void testSendKeysXlsx() {
        Utils.execute("xlsx", "sendKeys/sendKeys.xlsx");
    }
}
