package io.hugang.addselection;

import io.hugang.util.Utils;
import org.junit.Test;

public class AddSelectionTest {
    @Test
    public void testAddSelectionXlsx() {
        Utils.execute("xlsx", "addSelection.xlsx");
    }
    @Test
    public void testAddSelectionNotExistXlsx() {
        Utils.execute("xlsx", "addSelectionNotExist.xlsx");
    }
}
