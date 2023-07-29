package io.hugang.select;

import io.hugang.util.Utils;
import org.junit.Test;

public class SelectTest {
    @Test
    public void testSelectXlsx() {
        Utils.execute("xlsx", "select/select.xlsx");
    }
}
