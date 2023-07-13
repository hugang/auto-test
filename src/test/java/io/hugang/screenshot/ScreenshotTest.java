package io.hugang.screenshot;

import io.hugang.util.Utils;
import org.junit.Test;

public class ScreenshotTest {
    @Test
    public void testScreenshot() {
        Utils.execute("xlsx", "screenshot/screenshot.xlsx");
    }
}
