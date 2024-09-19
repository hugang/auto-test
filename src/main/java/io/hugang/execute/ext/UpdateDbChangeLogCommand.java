package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
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
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * updateDbChangeLog command
 * To apply database change log to other database, by liquibase.
 */
public class UpdateDbChangeLogCommand extends Command {

    public UpdateDbChangeLogCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "updateDbChangeLog";
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
        path = CommandExecuteUtil.getFilePathWithBaseDir(render(path));

        Connection connection;
        try {
            // 1. Establish database connection
            connection = db.getConnection();
            // 2. Wrap the connection in a Liquibase Database object
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            // 3. Initialize Liquibase with the changelog file and ClassLoader resource
            File changeLogFile = FileUtil.file(path);
            Liquibase liquibase = new Liquibase(changeLogFile.getName(), new DirectoryResourceAccessor(changeLogFile.getParentFile().toPath()), database);

            // 4. Apply the changes in the changelog to the database
            liquibase.update(new Contexts());

            connection.close();

        } catch (SQLException | LiquibaseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
