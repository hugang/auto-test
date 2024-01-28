package io.hugang.execute.ext;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
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
    public boolean _execute() {
        // get db from config
        String target = getTarget();
        DSFactory dsFactory;
        Setting setting = null;
        JSON json = null;
        // config from json
        try {
            json = JSONUtil.parse(this.getTarget());
            setting = new Setting();
            setting.put("url", json.getByPath("url").toString());
            setting.put("user", json.getByPath("user").toString());
            setting.put("pass", json.getByPath("pass").toString());
            if (ObjectUtil.isNotEmpty(json.getByPath("remark"))) {
                setting.put("remark", json.getByPath("remark").toString());
            }
        } catch (Exception e) {
            // not json
        }
        // config from path
        if (json == null) {
            setting = SettingUtil.get(target);
        }
        dsFactory = DSFactory.create(setting);
        Db db = Db.use(dsFactory.getDataSource());

        String path = this.getDictStr("path");
        String sqlStr = render(this.getDictStr("value", this.getValue())).replace("\n", "");
        path = this.getAutoTestConfig().getBaseDir().concat(render(path));
        ExcelWriter writer = ExcelUtil.getWriter(path);
        StyleSet styleSet = writer.getStyleSet();
        styleSet.setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);

        List<Entity> resultEntityList;
        try {
            writer.setCurrentRow(0);
            if (sqlStr.contains(";")) {
                String[] sqlArr = sqlStr.split(";");
                for (String sql : sqlArr) {
                    resultEntityList = db.query(sql);
                    Set<String> sqlSet = new HashSet<>(List.of(sql));
                    writer.write(sqlSet, false);
                    writer.write(resultEntityList, true);
                    writer.setCurrentRow(writer.getCurrentRow() + 1);
                }
            } else {
                resultEntityList = db.query(sqlStr);
                Set<String> sqlSet = new HashSet<>(List.of(sqlStr));
                writer.write(sqlSet, false);
                writer.write(resultEntityList, true);
            }
            writer.flush();
            writer.close();


        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
        return true;
    }
}
