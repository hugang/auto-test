package io.hugang.mouseOut;

import io.hugang.util.Utils;
import org.junit.Test;

public class MouseOutTest {
    @Test
    public void testMouseOutXlsx() {
        Utils.execute("xlsx", "mouseOut/mouseOut.xlsx");
    }
}
