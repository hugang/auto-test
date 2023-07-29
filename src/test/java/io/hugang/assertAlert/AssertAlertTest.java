package io.hugang.assertAlert;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertAlertTest {
    @Test
    public void testAddAlertXlsx() {
        Utils.execute("xlsx", "assertAlert/assertAlert.xlsx");
    }
}
