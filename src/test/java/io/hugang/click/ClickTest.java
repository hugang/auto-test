package io.hugang.click;

import io.hugang.BasicExecutor;
import io.hugang.open.OpenTest;
import org.junit.Test;

import java.util.Objects;

public class ClickTest {
    @Test
    public void testClickSide() {
        new BasicExecutor().execute("side", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("click/click.side")).getPath());
    }

    @Test
    public void testClickXlsx() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("click/click.xlsx")).getPath());
    }
    @Test
    public void testClickXlsxNotExist() {
        new BasicExecutor().execute("xlsx", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("click/clickNotExist.xlsx")).getPath());
    }

    @Test
    public void testClickCsv() {
        new BasicExecutor().execute("csv", Objects.requireNonNull(OpenTest.class.getClassLoader().getResource("click/click.csv")).getPath());
    }
}
