package io.hugang.execute.ext;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.DatabaseUtil;
import liquibase.change.ColumnConfig;
import liquibase.change.core.InsertDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Table;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * GenDbChangeLog command
 * To generate database change log from an existed database table, by liquibase.
 */
public class GenerateChangeLogCommand extends Command {

    public GenerateChangeLogCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "generateChangeLog";
    }

    @Override
    public boolean _execute() {
        Setting setting;
        // 获取类型
        String type = getDictStr("type", "");
        String target = getTarget();

        // 类型为json
        if ("json".equals(type)) {
            setting = new Setting();
            JSONObject entries = JSONUtil.parseObj(target);
            setting.put("url", entries.getStr("url"));
            setting.put("user", entries.getStr("user"));
            setting.put("pass", entries.getStr("pass"));
            if (ObjectUtil.isNotEmpty(entries.getStr("remark"))) {
                setting.put("remark", entries.getStr("remark"));
            }
        }
        // 类型为文件
        else if ("file".equals(type)) {
            setting = SettingUtil.get(target);
        }
        // 配置组
        else {
            Setting dbSetting = DatabaseUtil.getDbSetting();
            setting = dbSetting.getSetting(target);
        }
        DSFactory dsFactory;
        dsFactory = DSFactory.create(setting);
        Db db = Db.use(dsFactory.getDataSource());

        String path = this.getDictStr("path");
        String tableNames = render(this.getDictStr("value", this.getValue())).replace("\n", "");
        path = CommandExecuteUtil.getFilePathWithBaseDir(render(path));

        String genType = this.getDictStr("genType");

        if ("data".equals(genType)) {
            try {
                // 1. Create the change log object
                DatabaseChangeLog changeLog = new DatabaseChangeLog();
                // 2. Fetch existing records from the table
                if (tableNames.contains(";")) {
                    String[] sqlArr = tableNames.split(";");
                    for (String tableName : sqlArr) {
                        appendRecordToChangeLog(tableName, db, changeLog);
                    }
                } else {
                    appendRecordToChangeLog(tableNames, db, changeLog);
                }
                // 4. Write the changelog to the file system as XML
                try (PrintStream printStreamFile = new PrintStream(path)) {
                    new XMLChangeLogSerializer().write(changeLog.getChangeSets(), printStreamFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            generateTableChangeLog(db, path, tableNames);
        }
        return true;
    }

    private void appendRecordToChangeLog(String tableName, Db db, DatabaseChangeLog changeLog) throws SQLException {
        List<Entity> entities = db.query("select * from " + tableName);
        for (Entity entity : entities) {
            // Create a new change set for each insert
            String changeSetId = UUID.randomUUID().toString();
            ChangeSet changeSet = new ChangeSet(changeSetId, "author", false, false, null, null, null, true, null, changeLog);

            // Create an InsertDataChange object
            InsertDataChange insertChange = new InsertDataChange();
            insertChange.setTableName(tableName);
            for (String fieldName : entity.getFieldNames()) {
                ColumnConfig column = new ColumnConfig();
                column.setName(fieldName);
                setColumnValue(column, entity.get(fieldName));
                insertChange.addColumn(column);
            }
            // Add the insert change to the changeset
            changeSet.addChange(insertChange);
            // Add the changeset to the changelog
            changeLog.addChangeSet(changeSet);
        }
    }

    private void setColumnValue(ColumnConfig column, Object o) {
        if (o instanceof String) {
            column.setValue(o.toString());
        } else if (o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double) {
            column.setValueNumeric(o.toString());
        } else if (o instanceof Boolean) {
            column.setValueBoolean(o.toString());
        } else if (o instanceof Date) {
            column.setValueDate((Date) o);
        }
    }

    private void generateTableChangeLog(Db db, String path, String tableNames) {
        Connection connection;
        try {
            // 1. Establish database connection
            connection = db.getConnection();
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            // 2. Create snapshot for the existing table
            List<DatabaseObject> databaseObjects = new ArrayList<>();
            if (tableNames.contains(";")) {
                String[] sqlArr = tableNames.split(";");
                for (String tableName : sqlArr) {
                    databaseObjects.add(new Table(null, null, tableName));
                }
            } else {
                databaseObjects.add(new Table(null, null, tableNames));
            }
            DatabaseObject[] databaseObjects1 = new DatabaseObject[databaseObjects.size()];
            for (int i = 0; i < databaseObjects.size(); i++) {
                databaseObjects1[i] = databaseObjects.get(i);
            }

            DatabaseSnapshot snapshot = SnapshotGeneratorFactory.getInstance()
                    .createSnapshot(databaseObjects1, database, new SnapshotControl(database));

            // 3. Compare the snapshot with an empty database
            DiffResult diffResult = DiffGeneratorFactory.getInstance()
                    .compare(snapshot, null, new CompareControl());

            // 4. Generate the changelog
            DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, new DiffOutputControl());
            diffToChangeLog.print(path);

            connection.close();
        } catch (SQLException | DatabaseException | InvalidExampleException | IOException |
                 ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
