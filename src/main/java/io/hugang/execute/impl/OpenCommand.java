package io.hugang.execute.impl;

import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.execute.Command;

import java.util.UUID;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.screenshot;

@WebCommand
public class OpenCommand extends Command {
    public static final String KEY_URL = "url";

    public OpenCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public boolean _execute() {
        try {
            String url = this.getDictStr(KEY_URL);
            if (url == null) {
                url = this.getTarget();
            }
            String realUrl = render(url);
            open(realUrl);
            generateReportData();
            return true;
        } catch (Exception e) {
            throw new CommandExecuteException(e);
        }
    }

    private void generateReportData() {
        String reportImageName = UUID.randomUUID().toString();
        screenshot(this.getReportPath().concat("/").concat(reportImageName));
        this.appendReport(RESULT_TYPE_IMG, "./".concat(reportImageName).concat(".png"));
    }
}
