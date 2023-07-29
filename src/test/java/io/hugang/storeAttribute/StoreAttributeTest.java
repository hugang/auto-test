package io.hugang.storeAttribute;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreAttributeTest {
    @Test
    public void testStoreAttributeXlsx() {
        Utils.execute("xlsx", "storeAttribute/storeAttribute.xlsx");
    }
}
