package io.hugang.assertnotchecked;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertNotCheckedTest {
    @Test
    public void testAssertNotCheckedXlsx() {
        Utils.execute("xlsx", "assertnotchecked/assertNotChecked.xlsx");
    }
}
