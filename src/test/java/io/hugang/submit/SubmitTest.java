package io.hugang.submit;

import io.hugang.util.Utils;
import org.junit.Test;

public class SubmitTest {
    @Test
    public void testSubmitXlsx() {
        Utils.execute("xlsx", "submit/submit.xlsx");
    }
}
