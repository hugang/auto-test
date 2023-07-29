package io.hugang.storeValue;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreValueTest {
    @Test
    public void testStoreValueXlsx() {
        Utils.execute("xlsx", "storeValue/storeValue.xlsx");
    }
}
