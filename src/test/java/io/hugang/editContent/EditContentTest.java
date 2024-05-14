package io.hugang.editContent;

import org.junit.Test;

import io.hugang.util.Utils;

public class EditContentTest {
    @Test
    public void testEditContentXlsx() {
        Utils.execute("xlsx", "editContent/editContent.xlsx");
    }
}
