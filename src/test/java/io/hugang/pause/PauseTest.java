package io.hugang.pause;

import io.hugang.util.Utils;
import org.junit.Test;

public class PauseTest {
    @Test
    public void testPauseXlsx() {
        Utils.execute("xlsx","pause/pause.xlsx");
    }
}
