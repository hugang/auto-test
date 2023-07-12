package io.hugang.type;

import java.util.Objects;

import org.junit.Test;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;

public class TypeTest {
    @Test
    public void testTypeXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("type/type.xlsx")).getPath());
    }
    @Test
    public void testTypeErrorXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("type/typeError.xlsx")).getPath());
    }
}
