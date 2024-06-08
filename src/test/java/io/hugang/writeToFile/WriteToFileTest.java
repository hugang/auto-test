package io.hugang.writeToFile;

import io.hugang.util.Utils;
import org.junit.Test;

public class WriteToFileTest {
    @Test
    public void testWriteToFile() {
        Utils.execute("xlsx", "writeToFile/writeToFile.xlsm");
    }
}
