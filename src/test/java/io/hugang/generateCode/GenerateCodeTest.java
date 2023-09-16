package io.hugang.generateCode;

import io.hugang.util.Utils;
import org.junit.Test;

public class GenerateCodeTest {
    @Test
    public void testGenerateCodeXlsx() {
        Utils.execute("xlsx", "generateCode/generateCode.xlsx");
    }
}
