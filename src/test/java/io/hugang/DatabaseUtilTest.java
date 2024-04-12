package io.hugang;

import io.hugang.util.DatabaseUtil;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DatabaseUtilTest {

    @Test
    public void testGetDbSetting() {
        assertNotNull(DatabaseUtil.getDbSetting());
    }

    @Test
    public void testGetDbSettingPath() {
        assertNotNull(DatabaseUtil.getDbSettingPath());
    }

}
