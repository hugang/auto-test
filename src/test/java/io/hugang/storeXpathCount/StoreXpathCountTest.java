package io.hugang.storeXpathCount;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreXpathCountTest {
    @Test
    public void testStoreXpathCountXlsx() {
        Utils.execute("xlsx", "storeXpathCount/storeXpathCount.xlsx");
    }
}
