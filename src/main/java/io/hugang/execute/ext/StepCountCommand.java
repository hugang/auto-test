package io.hugang.execute.ext;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.log.Log;
import io.hugang.execute.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * command for step count
 * <p>
 * <br>target: target src paths, split by ','
 * <br>value: result path
 * <br>fileType: file type, split by ','
 * <br>excludePath: exclude path, split by ','
 * <br>excludeFile: exclude file type, split by ','
 */
public class StepCountCommand extends Command {
    private static final Log log = Log.get();

    public StepCountCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public String getCommand() {
        return "stepCount";
    }

    @Override
    public boolean _execute() {
        String[] targetPaths = render(this.getTarget()).split(",");
        String resultPath = getFilePath(render(this.getDictStr("value", this.getValue())));
        String[] fileType = getDictStr("fileType", "").split(",");
        String[] excludePath = getDictStr("excludePath", "").split(",");
        String[] excludeFile = getDictStr("excludeFile", "").split(",");

        FileWriter fileWriter = new FileWriter(resultPath);
        fileWriter.write("file,codeLines,commentLines,blankLines\n");

        for (String targetPath : targetPaths) {
            listAllFilesAndCountStep(targetPath, resultPath, fileType, excludePath, excludeFile, fileWriter);
        }
        this.appendDict("resultPath", resultPath);
        return true;
    }

    private void listAllFilesAndCountStep(String targetPath, String resultPath, String[] fileType, String[] excludePath, String[] excludeFile, FileWriter fileWriter) {
        // exclude path
        for (String path : excludePath) {
            if (targetPath.contains(path)) {
                return;
            }
        }

        File[] files = new File(targetPath).listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                listAllFilesAndCountStep(file.getAbsolutePath(), resultPath, fileType, excludePath, excludeFile, fileWriter);
            } else {
                // exclude file
                for (String fileStr : excludeFile) {
                    if (file.getName().endsWith(fileStr)) {
                        return;
                    }
                }
                // file type
                boolean isFileType = false;
                if (fileType.length == 0 || fileType[0].isEmpty() || fileType[0].equals("*")) {
                    isFileType = true;
                } else {
                    for (String type : fileType) {
                        if (file.getName().endsWith(type)) {
                            isFileType = true;
                            break;
                        }
                    }
                }
                countFileStep(fileWriter, file, isFileType);
            }
        }
    }

    private static void countFileStep(FileWriter fileWriter, File file, boolean isFileType) {
        try {
            if (isFileType && (file.getName().endsWith(".java")
                    || file.getName().endsWith(".js")
                    || file.getName().endsWith(".go"))) {
                // count code steps
                String line;
                int codeLines = 0;
                int commentLines = 0;
                int blankLines = 0;

                BufferedReader reader = new BufferedReader(new FileReader(file));
                boolean inComment = false;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    // start of comment
                    if (line.startsWith("/*")) {
                        inComment = true;
                    }
                    // code lines
                    if (!inComment && !line.startsWith("//") && !line.isEmpty()) {
                        codeLines++;
                    }
                    // blank lines
                    else if (!inComment && line.isEmpty()) {
                        blankLines++;
                    }
                    // comment lines
                    else {
                        commentLines++;
                    }
                    // end of comment
                    if (line.endsWith("*/")) {
                        inComment = false;
                    }
                }
                reader.close();
                fileWriter.append(file.getAbsolutePath() + "," + codeLines + "," + commentLines + "," + blankLines + "\n");

            } else {
                fileWriter.append(file.getAbsolutePath() + "," + Files.lines(file.toPath(), Charset.defaultCharset()).count() + ",0,0\n");
            }
        } catch (Exception e) {
            log.error(e);
        }
    }


}
