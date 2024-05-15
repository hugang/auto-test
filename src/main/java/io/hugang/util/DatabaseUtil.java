package io.hugang.util;

import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;

public class DatabaseUtil {

    public static Setting getDbSetting() {
        return SettingUtil.get(getDbSettingPath());
    }

    public static String getDbSettingPath() {
        return CommandExecuteUtil.getFilePathWithBaseDir("conf/db.conf");
    }
}
