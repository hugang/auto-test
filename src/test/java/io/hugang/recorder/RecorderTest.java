package io.hugang.recorder;

import io.hugang.util.Utils;
import org.junit.Test;

public class RecorderTest {
    @Test
    public void testRecorderXlsx() {
        Utils.execute("xlsx","recorder/recorder.xlsx");
    }
}
