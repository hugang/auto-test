package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import io.hugang.util.DatabaseUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class GenerateCodeCommand extends Command {

    public GenerateCodeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public String getCommand() {
        return "generateCode";
    }

    @Override
    public boolean _execute() {
        // set db setting path
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath());
        // get db from command target
        String dbName = this.getTarget();

        // parse value to json object
        Dict dictForTemplateRender = new Dict();
        //get template path
        String baseTemplatePath = getFilePath(this.getDictStr("template"));
        String moduleName = this.getDictStr("module");
        if (StrUtil.isNotEmpty(moduleName)) {
            dictForTemplateRender.set("moduleName", moduleName);
        } else {
            dictForTemplateRender.set("moduleName", "demo");
        }
        // get config.json from template path and parse to json object
        JSONObject templateConfig = JSONUtil.parseObj(FileUtil.readString(baseTemplatePath + File.separator + "config.json", CharsetUtil.CHARSET_UTF_8));
        // get project and developer from config.json

        Object project = templateConfig.get("project");
        if (ObjectUtil.isNotEmpty(project)) {
            JSONObject project1 = (JSONObject) project;
            // get all key-value from project and set to dictForTemplateRender
            project1.forEach(dictForTemplateRender::set);
        }
        Object developer = templateConfig.get("developer");
        if (ObjectUtil.isNotEmpty(developer)) {
            JSONObject developer1 = (JSONObject) developer;
            // get all key-value from project and set to dictForTemplateRender
            developer1.forEach(dictForTemplateRender::set);
        }
        dictForTemplateRender.putAll(getVariables());

        List<Dict> templateMap = new ArrayList<>();
        JSONArray templates = (JSONArray) templateConfig.get("templates");
        if (ObjectUtil.isNotEmpty(templates)) {
            for (Object template : templates) {
                JSONObject template1 = (JSONObject) template;
                Dict templatesMap = new Dict();
                // get all key-value from project and set to dictForTemplateRender
                template1.forEach(templatesMap::set);
                templateMap.add(templatesMap);
            }
        }


        for (String table : this.getDictStr("tables").split(",")) {
            Table tableMeta = MetaUtil.getTableMeta(DbUtil.getDs(dbName), table);
            if (ObjectUtil.isNull(tableMeta) || ObjectUtil.isEmpty(tableMeta.getColumns())) {
                continue;
            }
            maintainDictForTemplateRender(dictForTemplateRender, tableMeta);

            // loop all templates, get template path and target path
            for (Dict template : templateMap) {
                String templateName = template.getStr("templateName");
                String generatorPath = render(template.getStr("generatorPath"), dictForTemplateRender);
                // get template path
                String templatePath = getFilePath(baseTemplatePath + "/" + templateName);
                // render template
                String content = CommandExecuteUtil.render(FileUtil.readString(templatePath, CharsetUtil.CHARSET_UTF_8), dictForTemplateRender);
                // write to target path
                FileUtil.mkdir(FileUtil.file(generatorPath).getParent());
                FileUtil.writeString(content, generatorPath, CharsetUtil.CHARSET_UTF_8);
            }
        }
        return true;
    }

    private void maintainDictForTemplateRender(Dict dictForTemplateRender, Table tableMeta) {
        // get table name
        String tableName = tableMeta.getTableName();
        dictForTemplateRender.set("tableName", tableName);
        // change table name to camel case and set to dictForTemplateRender
        String camelCaseTableName = StrUtil.toCamelCase(tableName);
        dictForTemplateRender.set("ClassName", StrUtil.upperFirst(camelCaseTableName));
        // class name
        dictForTemplateRender.set("className", camelCaseTableName);
        // packagePath
        dictForTemplateRender.set("packagePath", StrUtil.replace(dictForTemplateRender.getStr("packageName"), ".", "/"));
        // packageName
        dictForTemplateRender.set("package", dictForTemplateRender.getStr("packageName"));
        // table comment
        dictForTemplateRender.set("tableComment", ObjectUtil.isEmpty(tableMeta.getComment()) ? tableMeta.getTableName() : tableMeta.getComment());
        // date format to yyyy-MM-dd
        dictForTemplateRender.set("date", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        // function name
        dictForTemplateRender.set("functionName", camelCaseTableName);
        // field list
        dictForTemplateRender.set("fieldList", tableMeta.getColumns());
    }
}
