package io.hugang;

import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.Table;
import cn.hutool.json.JSONUtil;
import io.hugang.util.DatabaseUtil;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseUtilTest {

    @Test
    public void testGetDbSetting() {
        assertNotNull(DatabaseUtil.getDbSetting());
    }

    @Test
    public void testGetDb1() throws SQLException {
        assertNotNull(DatabaseUtil.getDb());
    }

    @Test
    public void testGetDbWithName() {
        assertNotNull(DatabaseUtil.getDb("test"));
    }

    @Test
    public void testGetDbSettingPath() {
        assertNotNull(DatabaseUtil.getDbSettingPath());
    }

    @Test
    public void testGetTableInfo() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath());
        Table user1 = DatabaseUtil.getTableMeta(DSFactory.create(DatabaseUtil.getDbSetting().getSetting("test")).getDataSource(), "user1");
        assertNotNull(user1);
        assertEquals("user1", user1.getTableName());
        System.out.println(JSONUtil.toJsonPrettyStr(user1));
    }

    @Test
    public void testGetTables() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath());
        System.out.println(JSONUtil.toJsonPrettyStr(DatabaseUtil.getTables(DSFactory.create(DatabaseUtil.getDbSetting().getSetting("test")).getDataSource())));
    }
}
