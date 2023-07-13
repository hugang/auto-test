package io.hugang.click;

import io.hugang.util.Utils;
import org.junit.Test;

public class ClickTest {
    @Test
    public void testClickSide() {
        Utils.execute("side", "click.side");
    }

    @Test
    public void testClickXlsx() {
        Utils.execute("xlsx", "click.xlsx");
    }
    @Test
    public void testClickXlsxNotExist() {
        Utils.execute("xlsx", "clickNotExist.xlsx");
    }

    @Test
    public void testClickCsv() {
        Utils.execute("csv", "click.csv");
    }
}
