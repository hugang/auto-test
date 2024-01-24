package io.hugang.execute.ext;

import cn.hutool.core.lang.Dict;
import cn.hutool.db.meta.Table;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.DatabaseUtil;


public class GenerateCodeCommand extends Command {

    public GenerateCodeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // get db from config
        String dbName = this.getTarget();
        // parse value to json object
        Dict dict = new Dict();
        dict.putAll(this.getVariables());
        dict.putAll(getDict());

        for (String table : dict.getStr("tables").split(",")) {
            Table tableMeta = DatabaseUtil.getTableMeta(DatabaseUtil.getDb(this.getAutoTestConfig(), dbName), table);
            dict.set("table", tableMeta);

            System.out.println(CommandExecuteUtil.render("${table.tableName} columns : \n" +
                    "<#list table.columns as column> ${column.name}</#list>", dict));
        }
        return true;
    }
}
