package io.hugang.generateCode;

import cn.hutool.db.DbUtil;
import io.hugang.config.AutoTestConfig;
import io.hugang.util.DatabaseUtil;
import io.hugang.util.Utils;
import org.junit.Test;

public class GenerateCodeTest {
    @Test
    public void testGenerateCodeXlsx() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath(new AutoTestConfig()));
        Utils.execute("xlsx", "generateCode/generateCode.xlsx");
    }
}
