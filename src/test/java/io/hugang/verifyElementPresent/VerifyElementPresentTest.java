package io.hugang.verifyElementPresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class VerifyElementPresentTest {
    @Test
    public void testVerifyElementPresentXlsx() {
        Utils.execute("xlsx", "verifyElementPresent/verifyElementPresent.xlsx");
    }
}
