package io.hugang;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.json.JSONUtil;
import io.hugang.config.AutoTestConfig;
import io.hugang.util.DatabaseUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseUtilTest {

    @Test
    public void testGetDbSetting() {
        assertNotNull(DatabaseUtil.getDbSetting(new AutoTestConfig()));
    }

    @Test
    public void testGetDb1() {
        assertNotNull(DatabaseUtil.getDb(new AutoTestConfig()));
    }

    @Test
    public void testGetDbWithName() {
        assertNotNull(DatabaseUtil.getDb(new AutoTestConfig(), "test"));
    }

    @Test
    public void testGetDbSettingPath() {
        assertNotNull(DatabaseUtil.getDbSettingPath(new AutoTestConfig()));
    }

    @Test
    public void testGetTableInfo() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath(new AutoTestConfig()));
        Table user1 = DatabaseUtil.getTableMeta(Db.use("test"), "user1");
        assertNotNull(user1);
        assertEquals("user1", user1.getTableName());
        System.out.println(JSONUtil.toJsonPrettyStr(user1));
    }

    @Test
    public void testGetTables() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath(new AutoTestConfig()));
        System.out.println(JSONUtil.toJsonPrettyStr(DatabaseUtil.getTables(Db.use("test"))));
    }
}
