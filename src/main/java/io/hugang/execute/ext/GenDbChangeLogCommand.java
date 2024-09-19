package io.hugang.execute.ext;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.DatabaseUtil;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Table;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * GenDbChangeLog command
 * To generate database change log from an existed database table, by liquibase.
 */
public class GenDbChangeLogCommand extends Command {

    public GenDbChangeLogCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "genDbChangeLog";
    }

    @Override
    public boolean _execute() {
        Setting setting;
        // 获取类型
        String type = getDictStr("type","");
        String target = getDictStr("target");

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
        String sqlStr = render(this.getDictStr("value", this.getValue())).replace("\n", "");
        path = CommandExecuteUtil.getFilePathWithBaseDir(render(path));


        Connection connection;
        try {
            // 1. Establish database connection
            connection = db.getConnection();
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            // 2. Create snapshot for the existing table
            List<DatabaseObject> databaseObjects = new ArrayList<>();
            if (sqlStr.contains(";")) {
                String[] sqlArr = sqlStr.split(";");
                for (String tableName : sqlArr) {
                    databaseObjects.add(new Table(null,null,tableName));
                }
            }else {
                databaseObjects.add(new Table(null,null,sqlStr));
            }
            DatabaseObject[] databaseObjects1 = new DatabaseObject[databaseObjects.size()];
            for (int i = 0; i < databaseObjects.size(); i++) {
                databaseObjects1[i] = databaseObjects.get(i);
            }

            DatabaseSnapshot snapshot = SnapshotGeneratorFactory.getInstance()
                    .createSnapshot(databaseObjects1,database,new SnapshotControl(database));

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

        return true;
    }
}
