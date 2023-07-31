package io.hugang.removeSelection;

import com.codeborne.selenide.ex.ElementNotFound;
import io.hugang.util.Utils;
import org.junit.Test;

public class RemoveSelectionTest {
    @Test
    public void testRemoveSelectionXlsx() {
        Utils.execute("xlsx", "removeSelection/removeSelection.xlsx");
    }
}
