package io.hugang.answerOnNextPromptCommand;

import io.hugang.util.Utils;
import org.junit.Test;

public class AnswerOnNextPromptCommand {
    @Test
    public void testAnswerOnNextPromptCommandXlsx() {
        Utils.execute("xlsx", "answerOnNextPromptCommand/answerOnNextPromptCommand.xlsx");
    }
    
}
