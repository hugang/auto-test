package io.hugang.setWindowSize;

import io.hugang.exceptions.CommandExecuteException;
import io.hugang.util.Utils;
import org.junit.Test;

public class SetWindowSizeTest {
    @Test
    public void testSetWindowSizeXlsx() {
        Utils.execute("xlsx", "setWindowSize/setWindowSize.xlsx");
    }

    @Test
    public void testSetWindowSizeErrXlsx() {
        try {
            Utils.execute("xlsx", "setWindowSize/setWindowSizeError.xlsx");
        } catch (CommandExecuteException e) {
            assert e.getMessage().contains("NumberFormatException");
        }
    }
}
