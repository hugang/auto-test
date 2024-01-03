package io.hugang.mouseDown;

import io.hugang.util.Utils;
import org.junit.Test;

public class MouseDownTest {
    @Test
    public void testMouseDownXlsx() {
        Utils.execute("xlsx", "mouseDown/mouseDown.xlsx");
    }
}
