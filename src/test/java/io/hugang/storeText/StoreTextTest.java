package io.hugang.storeText;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreTextTest {
    @Test
    public void testStoreTextXlsx() {
        Utils.execute("xlsx", "storeText/storeText.xlsx");
    }
}
