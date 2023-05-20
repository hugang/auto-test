package io.hugang;

import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class AllTest {
    @Test
    public void testClickXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("testcase1.xlsx")).getPath());
    }    @Test
    public void testTimesXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("testcase2.xlsx")).getPath());
    }
}
