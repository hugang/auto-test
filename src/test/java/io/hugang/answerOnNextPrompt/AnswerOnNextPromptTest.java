package io.hugang.answerOnNextPrompt;

import io.hugang.util.Utils;
import org.junit.Test;

public class AnswerOnNextPromptTest {
    @Test
    public void testAnswerOnNextPromptCommandXlsx() {
        Utils.execute("xlsx", "answerOnNextPrompt/answerOnNextPrompt.xlsx");
    }
    
}
