package io.hugang.setProperty;

import io.hugang.util.Utils;
import org.junit.Test;

public class SetPropertyTest {
    @Test
    public void testSetPropertyXlsx() {
        Utils.execute("xlsx", "setProperty/setProperty.xlsx");
    }
}
