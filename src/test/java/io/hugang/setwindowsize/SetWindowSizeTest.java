package io.hugang.setwindowsize;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class SetWindowSizeTest {
    @Test
    public void testTypeXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("setwindowsize/setwindowsize.xlsx")).getPath());
    }
}
