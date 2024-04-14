package io.hugang;

import cn.hutool.db.DbUtil;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
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

    @Test
    public void testGetTableMeta() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath());
        Table tableMeta = MetaUtil.getTableMeta(DbUtil.getDs("test"), "gen_datasource");
        System.out.println(tableMeta);
    }
}
