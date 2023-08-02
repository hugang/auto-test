package io.hugang.close;

import org.junit.Test;

import io.hugang.util.Utils;

/**
 * CloseTest
 */
public class CloseTest {

    @Test
    public void testCloseExist() {
        Utils.execute("xlsx", "close/close.xlsm");
    }
}