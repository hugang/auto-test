package io.hugang.store;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreTest {
    @Test
    public void testStoreXlsx() {
        Utils.execute("xlsx","store/store.xlsx");
    }
}
