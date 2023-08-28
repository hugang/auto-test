package io.hugang.mouseUp;

import io.hugang.util.Utils;
import org.junit.Test;

public class MouseUpTest {
    @Test
    public void testMouseUpXlsx() {
        Utils.execute("xlsx", "mouseUp/mouseUp.xlsx");
    }
}
