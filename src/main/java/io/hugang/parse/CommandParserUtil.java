package io.hugang.parse;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.hugang.bean.Command;
import io.hugang.bean.Commands;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * input command parser util
 * <p>
 *
 * @author hugang
 */
public class CommandParserUtil {
    private static final Log log = LogFactory.get();

    /**
     * read one line and parse to command
     *
     * @param record record
     * @return command
     */
    public static Command getCommand(CSVRecord record) {
        Command command = new Command();

        command.setCommand(record.get(0));
        // set the target when split length is 2
        if (record.size() > 1) {
            command.setTarget(record.get(1));
        }
        // set the target and value when split length is 3
        if (record.size() > 2) {
            command.setValue(record.get(2));
        }
        return command;
    }

    /**
     * read from csv file and parse to command
     *
     * @return command list
     */
    public static List<Commands> getCommandsFromCsv(String csvFilePath) {
        List<Commands> commandsList = new ArrayList<>();
        List<Command> commandList = new ArrayList<>();
        Commands commands = new Commands();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            CSVParser csvRecords = new CSVParser(br, CSVFormat.DEFAULT.builder().setQuote('"').build());
            for (CSVRecord csvRecord : csvRecords) {
                commandList.add(CommandParserUtil.getCommand(csvRecord));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        commands.setCommands(commandList);
        commandsList.add(commands);
        return commandsList;
    }

    /**
     * read from xlsx file and parse to command
     *
     * @param xlsxFilePath xlsx file path
     * @return command list
     */
    public static List<Commands> getCommandsFromXlsx(String xlsxFilePath) {
        List<Commands> commandsList = new ArrayList<>();
        // first row is command, second row is target, third row is value or execute
        ExcelReader reader = ExcelUtil.getReader(xlsxFilePath, 0);

        // read the first row
        List<Object> commandRow = reader.readRow(0);
        // read the second row
        List<Object> targetRow = reader.readRow(1);
        // read the left rows
        for (int i = 2; i < reader.getRowCount(); i++) {
            List<Object> valueRow = reader.readRow(i);
            // judge the value row is empty or not
            String caseNo = valueRow.get(0).toString();
            if (ObjectUtil.isNotEmpty(valueRow) && ObjectUtil.isNotEmpty(valueRow.get(0)) && !caseNo.equals(StrUtil.EMPTY)) {
                // read the command row
                Commands commands = new Commands();
                List<Command> commandList = new ArrayList<>();
                for (int j = 1; j < commandRow.size(); j++) {
                    Command command = new Command();
                    command.setCommand(commandRow.get(j).toString());
                    command.setTarget(targetRow.get(j).toString());
                    command.setValue(valueRow.get(j).toString());
                    if (StrUtil.isNotEmpty(command.getValue())) {
                        commandList.add(command);
                    }
                }
                commands.setCaseId(caseNo);
                commands.setCommands(commandList);
                log.info("{}", commands);
                if (ObjectUtil.isNotEmpty(commandList)) {
                    commandsList.add(commands);
                }
            }
        }
        return commandsList;
    }

    /**
     * read from side file and parse to command
     *
     * @param sideFilePath side file path
     * @return command list
     */
    public static List<Commands> getCommandsFromSide(String sideFilePath) {
        // read side file and parse to json
        JSON json = JSONUtil.readJSON(new File(sideFilePath), CharsetUtil.CHARSET_UTF_8);
        // get the base url
        String baseUrl = (String) json.getByPath("url");
        // get the test case from json
        JSONArray tests = (JSONArray) json.getByPath("tests");
        List<Commands> commandsList = new ArrayList<>();
        for (Object o : tests) {
            Commands commands = new Commands();
            List<Command> commandList = new ArrayList<>();
            JSONArray test = (JSONArray) ((JSON) o).getByPath("commands");
            for (Object value : test) {
                Command command = new Command();
                JSON jsonCommand = (JSON) value;
                command.setCommand(jsonCommand.getByPath("command").toString());
                command.setTarget(jsonCommand.getByPath("target").toString());
                command.setValue(jsonCommand.getByPath("value").toString());
                if (command.getCommand().equals("open")) {
                    command.setTarget(baseUrl + command.getTarget());
                }
                commandList.add(command);
            }
            commands.setCommands(commandList);
            commandsList.add(commands);
        }
        return commandsList;
    }

    /**
     * save commands list to xlsx file
     *
     * @param commandsList     commands list
     * @param fileDownloadPath file download path
     */
    public static void saveCommandsListToXlsx(List<Commands> commandsList, String fileDownloadPath) {
        for (int i = 0; i < commandsList.size(); i++) {
            // create excel writer
            String destFilePath = fileDownloadPath + "/testcaseFromSide_" + System.currentTimeMillis() + "_" + (i + 1) + ".xlsx";
            log.info("save to xlsx: {}", destFilePath);

            ExcelWriter writer = ExcelUtil.getWriter(destFilePath);
            Commands commands = commandsList.get(i);

            writer.writeCellValue(0, 0, "TestCase");
            writer.writeCellValue(0, 2, i + 1);

            for (int i1 = 0; i1 < commands.getCommands().size(); i1++) {
                Command command = commands.getCommands().get(i1);
                // write command to first row
                writer.writeCellValue(i1 + 1, 0, command.getCommand());
                // write target to second row
                writer.writeCellValue(i1 + 1, 1, command.getTarget());
                // write value to third row
                writer.writeCellValue(i1 + 1, 2, command.getValue());
            }
            writer.flush();
            writer.close();
        }
    }
}
