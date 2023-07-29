package io.hugang.storeJson;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreJsonTest {
    @Test
    public void testStoreJsonXlsx() {
        Utils.execute("xlsx", "storeJson/storeJson.xlsx");
    }
}
