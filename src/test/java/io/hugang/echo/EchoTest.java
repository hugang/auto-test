package io.hugang.echo;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class EchoTest {
    @Test
    public void testAddSelectionXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("echo/echo.xlsx")).getPath());
    }
}
