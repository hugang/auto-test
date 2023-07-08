package io.hugang.addselection;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class AddSelectionTest {
    @Test
    public void testAddSelectionXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("addselection/addSelection.xlsx")).getPath());
    }
}
