package io.hugang.execute.impl;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import io.hugang.BasicExecutor;
import io.hugang.CommandExecuteException;
import io.hugang.bean.Command;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExportDbCommand extends Command {

    public ExportDbCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        DbUtil.setDbSettingPathGlobal(BasicExecutor.autoTestConfig.getWorkDir().concat("conf/db.conf"));
        // get db from config
        String dbName = this.getTarget();
        String path = this.getDictStr("path");
        String sqlStr = render(this.getDictStr("value", this.getValue())).replace("\n", "");
        path = BasicExecutor.autoTestConfig.getWorkDir().concat(render(path));
        ExcelWriter writer = ExcelUtil.getWriter(path);
        StyleSet styleSet = writer.getStyleSet();
        styleSet.setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

        List<Entity> resultEntiryList;
        try {
            writer.setCurrentRow(0);
            if (sqlStr.contains(";")) {
                String[] sqlArr = sqlStr.split(";");
                for (String sql : sqlArr) {
                    resultEntiryList = Db.use(dbName).query(sql);
                    Set<String> sqlSet = new HashSet<>(List.of(sql));
                    writer.write(sqlSet, false);
                    writer.write(resultEntiryList, true);
                    writer.setCurrentRow(writer.getCurrentRow() + 1);
                }
            } else {
                resultEntiryList = Db.use(dbName).query(sqlStr);
                Set<String> sqlSet = new HashSet<>(List.of(sqlStr));
                writer.write(sqlSet, false);
                writer.write(resultEntiryList, true);
            }
            writer.flush();
            writer.close();


        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
