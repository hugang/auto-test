package io.hugang.assertalert;

import io.hugang.util.Utils;
import org.junit.Test;

public class AssertAlertTest {
    @Test
    public void testAddAlertXlsx() {
        Utils.execute("xlsx", "assertalert/assertAlert.xlsx");
    }
}
