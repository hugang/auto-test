package io.hugang.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.Table;
import cn.hutool.log.Log;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.db.DbUtil.close;

public class DatabaseUtil {
    private final static Log log = Log.get();

    public static Setting getDbSetting() {
        return SettingUtil.get(getDbSettingPath());
    }

    public static Db getDb() {
        return getDb(null);
    }

    public static Db getDb(String dbName) {
        if (dbName == null || dbName.isEmpty()) {
            return Db.use(DSFactory.create(getDbSetting()).getDataSource());
        } else {
            return Db.use(dbName);
        }
    }

    public static String getDbSettingPath() {
        return BasicExecutor.autoTestConfig.getWorkDir().concat("conf/db.conf");
    }

    public static List<String> getTables(Db db) {
        try {
            return getTables(db.getConnection());
        } catch (SQLException e) {
            log.error(e);
            throw new CommandExecuteException(e);
        }
    }

    /**
     * 获得所有表名
     *
     * @param conn 数据源连接
     * @return 表名列表
     */
    public static List<String> getTables(Connection conn) {
        final List<String> tables = new ArrayList<>();
        ResultSet rs = null;
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
            if (rs == null) {
                return null;
            }
            while (rs.next()) {
                final String table = rs.getString("TABLE_NAME");
                if (!StrUtil.isBlank(table)) {
                    tables.add(table);
                }
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            close(rs, conn);
        }
        return tables;
    }

    public static Table getTableMeta(Db db, String tableName) {
        try {
            return getTableMeta(db.getConnection(), tableName);
        } catch (SQLException e) {
            log.error(e);
            throw new CommandExecuteException(e);
        }
    }

    public static Table getTableMeta(Connection conn, String tableName) {
        final Table table = Table.create(tableName);
        ResultSet rs = null;
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            //获得主键
            rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                table.addPk(rs.getString("COLUMN_NAME"));
            }
            //获得列
            rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                table.setColumn(Column.create(table, rs));
            }
        } catch (SQLException e) {
            log.error(e);
            throw new CommandExecuteException(e);
        } finally {
            close(rs, conn);
        }
        return table;
    }
}
