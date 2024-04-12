package io.hugang.generateCode;

import cn.hutool.db.DbUtil;
import io.hugang.util.DatabaseUtil;
import io.hugang.util.Utils;
import org.junit.Test;

public class GenerateCodeTest {
    @Test
    public void testGenerateCodeXlsx() {
        DbUtil.setDbSettingPathGlobal(DatabaseUtil.getDbSettingPath());
        Utils.execute("xlsx", "generateCode/generateCode.xlsx");
    }
}
