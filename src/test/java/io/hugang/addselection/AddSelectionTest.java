package io.hugang.addselection;

import com.codeborne.selenide.ex.ElementNotFound;
import io.hugang.util.Utils;
import org.junit.Test;

public class AddSelectionTest {
    @Test
    public void testAddSelectionXlsx() {
        Utils.execute("xlsx", "addselection/addSelection.xlsx");
    }

    @Test(expected = ElementNotFound.class)
    public void testAddSelectionNotExistXlsx() {
        Utils.execute("xlsx", "addselection/addSelectionNotExist.xlsx");
    }
}
