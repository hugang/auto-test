package io.hugang.assertelementnotpresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertElementNotPresentTest {
    @Test
    public void testAssertElementNotPresentXlsx() {
        Utils.execute("xlsx", "assertelementnotpresent/assertElementNotPresent.xlsx");
    }
}
