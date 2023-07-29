package io.hugang.assertChecked;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertCheckedTest {
    @Test
    public void testAssertCheckedXlsx() {
        Utils.execute("xlsx", "assertChecked/assertChecked.xlsx");
    }
}
