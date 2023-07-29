package io.hugang.assertElementNotPresent;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertElementNotPresentTest {
    @Test
    public void testAssertElementNotPresentXlsx() {
        Utils.execute("xlsx", "assertElementNotPresent/assertElementNotPresent.xlsx");
    }
}
