package io.hugang.assertConfirmation;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertConfirmationTest {
    @Test
    public void testAssertConfirmationXlsx() {
        Utils.execute("xlsx", "assertConfirmation/assertConfirmation.xlsx");
    }
}
