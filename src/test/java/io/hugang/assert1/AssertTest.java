package io.hugang.assert1;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertTest {
    @Test
    public void testAssertXlsx() {
        Utils.execute("xlsx", "assert.xlsx");
    }
    @Test
    public void testAssertErrorXlsx() {
        Utils.execute("xlsx", "assertError.xlsx");
    }
}
