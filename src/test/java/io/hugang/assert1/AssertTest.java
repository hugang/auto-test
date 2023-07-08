package io.hugang.assert1;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class AssertTest {
    @Test
    public void testAssertXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("assert1/assert.xlsx")).getPath());
    }
    @Test
    public void testAssertErrorXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("assert1/assertError.xlsx")).getPath());
    }
}
