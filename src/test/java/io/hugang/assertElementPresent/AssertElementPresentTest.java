package io.hugang.assertElementPresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertElementPresentTest {
    @Test
    public void testAssertElementPresentXlsx() {
        Utils.execute("xlsx", "assertElementPresent/assertElementPresent.xlsx");
    }
}
