package io.hugang.mouseOver;

import io.hugang.util.Utils;
import org.junit.Test;

public class MouseOverTest {
    @Test
    public void testMouseOverXlsx() {
        Utils.execute("xlsx", "mouseOver/mouseOver.xlsx");
    }
}
