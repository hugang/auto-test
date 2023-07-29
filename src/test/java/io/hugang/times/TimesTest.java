package io.hugang.times;

import io.hugang.util.Utils;
import org.junit.Test;

public class TimesTest {
    @Test
    public void testTimesXlsx() {
        Utils.execute("xlsx", "times/times.xlsx");
    }
}
