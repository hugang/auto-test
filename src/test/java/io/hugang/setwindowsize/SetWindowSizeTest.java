package io.hugang.setwindowsize;

import io.hugang.util.Utils;
import org.junit.Test;

public class SetWindowSizeTest {
    @Test
    public void testSetWindowSizeXlsx() {
        Utils.execute("xlsx", "setwindowsize/setwindowsize.xlsx");
    }

    @Test
    public void testSetWindowSizeErrXlsx() {
        Utils.execute("xlsx", "setwindowsize/setwindowsizeError.xlsx");
    }
}
