package io.hugang.callApi;

import io.hugang.util.Utils;
import org.junit.Test;

public class CallApiTest {
    @Test
    public void testCallApiXlsx() {
        Utils.execute("xlsx", "callApi/callApi.xlsx");
    }
}
