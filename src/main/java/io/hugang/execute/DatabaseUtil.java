package io.hugang.execute;

import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.BasicExecutor;

public class DatabaseUtil {
    public static Setting getDbSetting() {
        return SettingUtil.get(BasicExecutor.autoTestConfig.getWorkDir().concat("conf/db.conf"));
    }
}
