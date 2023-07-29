package io.hugang.selectFrame;

import io.hugang.util.Utils;
import org.junit.Test;

public class SelectFrameTest {
    @Test
    public void testSelectFrameXlsx() {
        Utils.execute("xlsx", "selectFrame/selectFrame.xlsx");
    }
}
