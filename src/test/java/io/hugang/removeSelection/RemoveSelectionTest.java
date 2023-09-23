package io.hugang.removeSelection;

import io.hugang.util.Utils;
import org.junit.Test;

public class RemoveSelectionTest {
    @Test
    public void testRemoveSelectionXlsx() {
        Utils.execute("xlsx", "removeSelection/removeSelection.xlsx");
    }
}
