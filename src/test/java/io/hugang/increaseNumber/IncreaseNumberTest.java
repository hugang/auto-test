package io.hugang.increaseNumber;

import io.hugang.util.Utils;
import org.junit.Test;

public class IncreaseNumberTest {
    @Test
    public void testIncreaseNumberXlsx() {
        Utils.execute("xlsx", "increaseNumber/increaseNumber.xlsx");
    }
}
