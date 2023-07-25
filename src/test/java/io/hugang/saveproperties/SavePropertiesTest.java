package io.hugang.saveproperties;

import io.hugang.execute.CommandExecuteUtil;
import io.hugang.util.Utils;
import org.junit.Test;

public class SavePropertiesTest {

    @Test
    public void testSavePropertiesXlsx() {
        Utils.execute("xlsx", "saveproperties/saveProperties.xlsx");
    }
}
