package io.hugang.chooseOkOnNextConfirmation;

import io.hugang.util.Utils;
import org.junit.Test;

public class ChooseOkOnNextConfirmationTest {
    @Test
    public void testChooseOkOnNextConfirmationXlsx() {
        Utils.execute("xlsx", "assertConfirmation/assertConfirmation.xlsx");
    }
}
