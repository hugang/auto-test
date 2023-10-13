package io.hugang;

import io.hugang.util.Utils;
import org.junit.Test;

public class ModeTest {
    @Test
    public void testCsv() {
        Utils.execute("csv", "csv/example.csv");
    }
    @Test
    public void testJson() {
        Utils.execute("json", "json/example.json");
    }
}
