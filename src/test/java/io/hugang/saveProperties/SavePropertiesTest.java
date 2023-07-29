package io.hugang.saveProperties;

import io.hugang.util.Utils;
import org.junit.Test;

public class SavePropertiesTest {

    @Test
    public void testSavePropertiesXlsx() {
        Utils.execute("xlsx", "saveProperties/saveProperties.xlsx");
    }
}
