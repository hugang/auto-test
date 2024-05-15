package io.hugang.execute.ext;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.DatabaseUtil;
import io.hugang.util.ThreadContext;
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
    public String getCommand() {
        return "exportDb";
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
