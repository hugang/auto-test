package io.hugang.assertchecked;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertCheckedTest {
    @Test
    public void testAssertCheckedXlsx() {
        Utils.execute("xlsx", "assertchecked/assertChecked.xlsx");
    }
}
