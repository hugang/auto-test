package io.hugang.selectWindow;

import io.hugang.util.Utils;
import org.junit.Test;

public class SelectWindowTest {
    @Test
    public void testSelectWindowXlsx() {
        Utils.execute("xlsx", "selectWindow/selectWindow.xlsx");
    }
}
