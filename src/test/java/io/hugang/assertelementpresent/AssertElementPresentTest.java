package io.hugang.assertelementpresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertElementPresentTest {
    @Test
    public void testAssertElementPresentXlsx() {
        Utils.execute("xlsx", "assertelementpresent/assertElementPresent.xlsx");
    }
}
