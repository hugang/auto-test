package io.hugang.click;

import io.hugang.util.Utils;
import org.junit.Test;

public class ClickTest {
    @Test
    public void testClickXlsx() {
        Utils.execute("xlsx", "click/click.xlsx");
    }
    @Test
    public void testClickXlsxNotExist() {
        Utils.execute("xlsx", "click/clickNotExist.xlsx");
    }

    @Test
    public void testClickCsv() {
        Utils.execute("csv", "click/click.csv");
    }
}
