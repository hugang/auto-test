package io.hugang.exportDb;

import io.hugang.util.Utils;
import org.junit.Test;

public class ExportDbTest {
    @Test
    public void testExportDbXlsx() {
        Utils.execute("xlsx", "exportDb/exportDb.xlsx");
    }
}
