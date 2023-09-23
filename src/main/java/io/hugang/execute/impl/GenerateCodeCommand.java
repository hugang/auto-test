package io.hugang.execute.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.hugang.bean.Command;
import io.hugang.execute.DatabaseUtil;

import java.util.Collections;

public class GenerateCodeCommand extends Command {

    public GenerateCodeCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean execute() {
        Setting setting = DatabaseUtil.getDbSetting();
        // get db from config
        String dbName = this.getTarget();
        String url = setting.get(dbName, "url");
        String user = setting.get(dbName, "user");
        String pass = setting.get(dbName, "pass");

        String value = this.getDictStr("value", this.getValue());
        // parse value to json object
        JSONObject options = (JSONObject) JSONUtil.parse(value);
        if (options.getStr("url") != null) {
            url = options.getStr("url");
        }
        if (options.getStr("user") != null) {
            user = options.getStr("user");
        }
        if (options.getStr("pass") != null) {
            pass = options.getStr("pass");
        }
        String path = options.getStr("path");
        String authorStr = options.getStr("author");
        String packageStr = options.getStr("package");
        String basePackageStr = options.getStr("basePackage");
        String tablesStr = options.getStr("tables");

        FastAutoGenerator.create(url, user, pass)
                .globalConfig(builder -> {
                    // 设置作者
                    builder.author(authorStr)
                            // 指定输出目录
                            .outputDir(path);
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent(packageStr)
                            // 设置父包模块名
                            .moduleName(basePackageStr)
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, path));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude(tablesStr)
                            // 设置过滤表前缀
                            .addTablePrefix("t_", "c_");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板,可以自定义模板引擎进行功能扩展
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(builder -> builder.controller("/templates/Controller"))
                .execute();
        return true;
    }
}
