package io.hugang.calc;

import io.hugang.util.Utils;
import org.junit.Test;

public class CalcTest {
    @Test
    public void testCalcXlsx() {
        Utils.execute("xlsx", "calc/calc.xlsm");
    }
}
