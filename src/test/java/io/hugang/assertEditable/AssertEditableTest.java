package io.hugang.assertEditable;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertEditableTest {
    @Test
    public void testAssertEditableXlsx() {
        Utils.execute("xlsx", "assertEditable/assertEditable.xlsx");
    }
}
