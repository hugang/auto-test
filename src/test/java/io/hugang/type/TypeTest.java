package io.hugang.type;

import io.hugang.exceptions.CommandExecuteException;
import io.hugang.util.Utils;
import org.junit.Test;

public class TypeTest {
    @Test
    public void testTypeXlsx() {
        Utils.execute("xlsx", "type/type.xlsx");
    }

    @Test
    public void testTypeErrorXlsx() {
        // BasicExecutor中已经捕捉了CommandExecuteException，所以这里不会抛出异常
        Utils.execute("xlsx", "type/typeError.xlsx");
    }
}
