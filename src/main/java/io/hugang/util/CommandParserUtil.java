package io.hugang.util;

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
import io.hugang.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.*;
import io.hugang.execute.impl.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    public static OriginalCommand getCommand(CSVRecord record) {
        OriginalCommand command = new OriginalCommand();

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
        List<OriginalCommand> commandList = new ArrayList<>();
        Commands commands = new Commands();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            CSVParser csvRecords = new CSVParser(br, CSVFormat.DEFAULT.builder().setQuote('"').build());
            for (CSVRecord csvRecord : csvRecords) {
                commandList.add(CommandParserUtil.getCommand(csvRecord));
            }
        } catch (IOException e) {
            throw new CommandExecuteException("read csv file error");
        }
        parseCommandToSubCommand(commandList, commands);
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
                List<OriginalCommand> commandList = new ArrayList<>();
                for (int j = 1; j < commandRow.size(); j++) {
                    OriginalCommand command = new OriginalCommand();
                    command.setCommand(commandRow.get(j).toString());
                    if (ObjectUtil.isNotEmpty(targetRow) && ObjectUtil.isNotEmpty(targetRow.get(j))) {
                        command.setTarget(targetRow.get(j).toString());
                    }
                    if (ObjectUtil.isNotEmpty(valueRow) && ObjectUtil.isNotEmpty(valueRow.get(j))) {
                        command.setValue(valueRow.get(j).toString());
                    }
                    commandList.add(command);
                }
                commands.setCaseId(caseNo);
                parseCommandToSubCommand(commandList, commands);
                if (ObjectUtil.isNotEmpty(commandList)) {
                    commandsList.add(commands);
                }
            }
        }
        return commandsList;
    }

    public static void parseCommandToSubCommand(List<OriginalCommand> commandList, Commands newCommandList) {
        List<ICommand> commands = new ArrayList<>();
        Stack<IConditionCommand> commandStack = new Stack<>();
        boolean isWeb = false;

        for (OriginalCommand command : commandList) {
            String commandName = command.getCommand();
            switch (commandName) {
                case "if":
                    IfCommand ifCommand = new IfCommand(commandName, command.getTarget(), command.getValue());
                    commandStack.push(ifCommand);
                    break;
                case "times":
                    TimesCommand timesCommand = new TimesCommand(commandName, command.getTarget(), command.getValue());
                    commandStack.push(timesCommand);
                    break;
                case "while":
                    WhileCommand whileCommand = new WhileCommand(commandName, command.getTarget(), command.getValue());
                    commandStack.push(whileCommand);
                    break;
                case "forEach":
                    ForEachCommand forEachCommand = new ForEachCommand(commandName, command.getTarget(), command.getValue());
                    commandStack.push(forEachCommand);
                    break;
                case "end":
                    ICommand subCommand = commandStack.pop();
                    if (!commandStack.empty()) {
                        commandStack.peek().addSubCommand(subCommand);
                    } else {
                        commands.add(subCommand);
                    }
                    break;
                default:
                    ICommand normalCommand = parseOriginToCommand(command);
                    // if the command with WebCommand annotation, set the isWeb to true
                    assert normalCommand != null;
                    if (normalCommand.getClass().isAnnotationPresent(WebCommand.class)) {
                        isWeb = true;
                    }

                    if (!commandStack.empty()) {
                        commandStack.peek().addSubCommand(normalCommand);
                    } else {
                        commands.add(normalCommand);
                    }
                    break;
            }
        }
        newCommandList.setCommands(commands);
        newCommandList.setWebCommand(isWeb);
    }

    private static ICommand parseOriginToCommand(OriginalCommand command) {
        String commandName = command.getCommand();
        switch (commandName) {
            case "click":
                return new ClickCommand(commandName, command.getTarget(), command.getValue());
            case "type":
                return new TypeCommand(commandName, command.getTarget(), command.getValue());
            case "select":
                return new SelectCommand(commandName, command.getTarget(), command.getValue());
            case "selectFrame":
                return new SelectFrameCommand(commandName, command.getTarget(), command.getValue());
            case "selectWindow":
                return new SelectWindowCommand(commandName, command.getTarget(), command.getValue());
            case "open":
                return new OpenCommand(commandName, command.getTarget(), command.getValue());
            case "run":
                return new RunCommand(commandName, command.getTarget(), command.getValue());
            case "runScript":
                return new RunScriptCommand(commandName, command.getTarget(), command.getValue());
            case "echo":
                return new EchoCommand(commandName, command.getTarget(), command.getValue());
            case "setProperty":
                return new SetPropertyCommand(commandName, command.getTarget(), command.getValue());
            case "readProperties":
                return new ReadPropertiesCommand(commandName, command.getTarget(), command.getValue());
            case "saveProperties":
                return new SavePropertiesCommand(commandName, command.getTarget(), command.getValue());
            case "increaseNumber":
                return new IncreaseNumberCommand(commandName, command.getTarget(), command.getValue());
            case "setWindowSize":
                return new SetWindowSizeCommand(commandName, command.getTarget(), command.getValue());
            case "waitForText":
                return new WaitForTextCommand(commandName, command.getTarget(), command.getValue());
            case "sendKeys":
                return new SendKeysCommand(commandName, command.getTarget(), command.getValue());
            case "screenshot":
                return new ScreenshotCommand(commandName, command.getTarget(), command.getValue());
            case "submit":
                return new SubmitCommand(commandName, command.getTarget(), command.getValue());
            case "pause":
            case "wait":
            case "sleep":
                return new PauseCommand(commandName, command.getTarget(), command.getValue());
            case "assert":
                return new AssertCommand(commandName, command.getTarget(), command.getValue());
            case "assertAlert":
                return new AssertAlertCommand(commandName, command.getTarget(), command.getValue());
            case "assertElementPresent":
                return new AssertElementPresentCommand(commandName, command.getTarget(), command.getValue());
            case "assertElementNotPresent":
                return new AssertElementNotPresentCommand(commandName, command.getTarget(), command.getValue());
            case "assertChecked":
                return new AssertCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotChecked":
                return new AssertNotCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "clickAt":
                return new ClickAtCommand(commandName, command.getTarget(), command.getValue());
            case "doubleClickAt":
                return new DoubleClickAtCommand(commandName, command.getTarget(), command.getValue());
            case "doubleClick":
                return new DoubleClickCommand(commandName, command.getTarget(), command.getValue());
            case "runCase":
                return new RunCaseCommand(commandName, command.getTarget(), command.getValue());
            case "addSelection":
                return new AddSelectionCommand(commandName, command.getTarget(), command.getValue());
            case "removeSelection":
                return new RemoveSelectionCommand(commandName, command.getTarget(), command.getValue());
            case "executeScript":
                return new ExecuteScriptCommand(commandName, command.getTarget(), command.getValue());
            case "executeAsyncScript":
                return new ExecuteAsyncScriptCommand(commandName, command.getTarget(), command.getValue());
            case "mouseOver":
                return new MouseOverCommand(commandName, command.getTarget(), command.getValue());
            case "mouseUp":
                return new MouseUpCommand(commandName, command.getTarget(), command.getValue());
            case "mouseDown":
                return new MouseDownCommand(commandName, command.getTarget(), command.getValue());
            case "store":
                return new StoreCommand(commandName, command.getTarget(), command.getValue());
            case "storeText":
                return new StoreTextCommand(commandName, command.getTarget(), command.getValue());
            case "storeTitle":
                return new StoreTitleCommand(commandName, command.getTarget(), command.getValue());
            case "storeValue":
                return new StoreValueCommand(commandName, command.getTarget(), command.getValue());
            case "storeAttribute":
                return new StoreAttributeCommand(commandName, command.getTarget(), command.getValue());
            case "storeJson":
                return new StoreJsonCommand(commandName, command.getTarget(), command.getValue());
            case "storeXpathCount":
                return new StoreXpathCountCommand(commandName, command.getTarget(), command.getValue());
            case "callApi":
                return new CallApiCommand(commandName, command.getTarget(), command.getValue());
            case "jenkinsJob":
                return new JenkinsJobCommand(commandName, command.getTarget(), command.getValue());
            case "answerOnNextPrompt":
                return new AnswerOnNextPromptCommand(commandName, command.getTarget(), command.getValue());
            case "check":
                return new CheckCommand(commandName, command.getTarget(), command.getValue());
            case "uncheck":
                return new UncheckCommand(commandName, command.getTarget(), command.getValue());
            case "mouseOut":
                return new MouseOutCommand(commandName, command.getTarget(), command.getValue());
            case "close":
                return new CloseCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementPresent":
                return new WaitForElementPresentCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementVisible":
                return new WaitForElementVisibleCommand(commandName, command.getTarget(), command.getValue());
            case "generateCode":
                return new GenerateCodeCommand(commandName, command.getTarget(), command.getValue());
            case "exportDb":
                return new ExportDbCommand(commandName, command.getTarget(), command.getValue());
            default:
                break;
        }
        return null;
    }

    /**
     * read from json file and parse to command
     *
     * @param jsonFilePath json file path
     * @return command list
     */
    public static List<Commands> getCommandsFromJson(String jsonFilePath) {
        // read json file and parse to json
        JSON json = JSONUtil.readJSON(new File(jsonFilePath), CharsetUtil.CHARSET_UTF_8);
        // get the base url
        String baseUrl = (String) json.getByPath("url");
        // get the test case from json
        JSONArray tests = (JSONArray) json.getByPath("tests");
        List<Commands> commandsList = new ArrayList<>();
        for (Object o : tests) {
            Commands commands = new Commands();
            List<OriginalCommand> commandList = new ArrayList<>();
            JSONArray test = (JSONArray) ((JSON) o).getByPath("commands");
            for (Object value : test) {
                OriginalCommand command = new OriginalCommand();
                JSON jsonCommand = (JSON) value;
                command.setCommand(jsonCommand.getByPath("command").toString());
                if (ObjectUtil.isNotEmpty(jsonCommand.getByPath("target"))) {
                    command.setTarget(jsonCommand.getByPath("target").toString());
                }
                if (ObjectUtil.isNotEmpty(jsonCommand.getByPath("value"))) {
                    command.setValue(jsonCommand.getByPath("value").toString());
                }
                if (command.getCommand().equals("open")) {
                    command.setTarget(baseUrl + command.getTarget());
                }
                commandList.add(command);
            }
            parseCommandToSubCommand(commandList, commands);
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
            String destFilePath = fileDownloadPath + "/testcaseFromJson_" + System.currentTimeMillis() + "_" + (i + 1) + ".xlsx";
            log.info("save to xlsx: {}", destFilePath);

            ExcelWriter writer = ExcelUtil.getWriter(destFilePath);
            Commands commands = commandsList.get(i);

            writer.writeCellValue(0, 0, "TestCase");
            writer.writeCellValue(0, 2, i + 1);

            for (int i1 = 0; i1 < commands.getCommands().size(); i1++) {
                Command command = (Command) commands.getCommands().get(i1);
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
