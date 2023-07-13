package io.hugang.type;

import io.hugang.util.Utils;
import org.junit.Test;

public class TypeTest {
    @Test
    public void testTypeXlsx() {
        Utils.execute("xlsx", "type/type.xlsx");
    }

    @Test
    public void testTypeErrorXlsx() {
        Utils.execute("xlsx", "type/typeError.xlsx");
    }
}
