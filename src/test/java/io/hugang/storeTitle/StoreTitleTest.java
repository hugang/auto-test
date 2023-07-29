package io.hugang.storeTitle;

import io.hugang.util.Utils;
import org.junit.Test;

public class StoreTitleTest {
    @Test
    public void testStoreTitleXlsx() {
        Utils.execute("xlsx", "storeTitle/storeTitle.xlsx");
    }
}
