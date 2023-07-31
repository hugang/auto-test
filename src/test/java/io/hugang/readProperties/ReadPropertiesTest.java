package io.hugang.readProperties;

import io.hugang.execute.CommandExecuteUtil;
import io.hugang.util.Utils;
import org.junit.Test;

public class ReadPropertiesTest {

    @Test
    public void testReadPropertiesXlsx() {
        Utils.execute("xlsx", "readProperties/readProperties.xlsx");
    }
}
