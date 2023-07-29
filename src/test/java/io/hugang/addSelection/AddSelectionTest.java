package io.hugang.addSelection;

import com.codeborne.selenide.ex.ElementNotFound;
import io.hugang.util.Utils;
import org.junit.Test;

public class AddSelectionTest {
    @Test
    public void testAddSelectionXlsx() {
        Utils.execute("xlsx", "addSelection/addSelection.xlsx");
    }

    @Test(expected = ElementNotFound.class)
    public void testAddSelectionNotExistXlsx() {
        Utils.execute("xlsx", "addSelection/addSelectionNotExist.xlsx");
    }
}
