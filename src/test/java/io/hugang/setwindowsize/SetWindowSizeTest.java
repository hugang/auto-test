package io.hugang.setwindowsize;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class SetWindowSizeTest {
    @Test
    public void testSetWindowSizeXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("setwindowsize/setwindowsize.xlsx")).getPath());
    }    @Test
    public void testSetWindowSizeErrXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("setwindowsize/setwindowsizeError.xlsx")).getPath());
    }
}
