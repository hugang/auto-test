package io.hugang.echo;

import io.hugang.util.Utils;
import org.junit.Test;

public class EchoTest {
    @Test
    public void testEchoXlsx() {
        Utils.execute("xlsx", "echo/echo.xlsx");
    }
}
