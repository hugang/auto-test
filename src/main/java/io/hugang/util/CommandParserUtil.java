package io.hugang.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.hugang.exceptions.AutoTestException;
import io.hugang.exceptions.CommandExecuteException;
import io.hugang.annotation.WebCommand;
import io.hugang.bean.*;
import io.hugang.execute.Command;
import io.hugang.execute.Commands;
import io.hugang.execute.ICommand;
import io.hugang.execute.IConditionCommand;
import io.hugang.execute.condition.*;
import io.hugang.execute.impl.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * input command parser util
 * <p>
 *
 * @author hugang
 */
public class CommandParserUtil {
    private static final Log log = Log.get();
    private static final Map<String, Class<?>> EXT_COMMAND_CLASS_MAP = new HashMap<>();

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

        int startLineNo = 0;
        // read the first row
        List<Object> commandRow = reader.readRow(0);
        if (ObjectUtil.isEmpty(commandRow) || ObjectUtil.isEmpty(commandRow.get(0))) {
            // if the first row is comment line, set the start line number to 1
            startLineNo = 1;
            commandRow = reader.readRow(startLineNo);
        }

        // read the second row
        List<Object> targetRow = reader.readRow(startLineNo + 1);
        // read the left rows
        for (int i = startLineNo + 2; i < reader.getRowCount(); i++) {
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
                    ifCommand.setUuid(UUID.randomUUID().toString());
                    commandStack.push(ifCommand);
                    break;
                case "elseIf":
                    // if the command stack is empty and the latest stack command is not if or elseIf, throw exception
                    if (commandStack.empty() || !("if".equals(commandStack.peek().getCommand()) || "elseIf".equals(commandStack.peek().getCommand()))) {
                        throw new CommandExecuteException("elseIf command must be after if or elseIf command");
                    }
                    ElseIfCommand elseIfCommand = new ElseIfCommand(commandName, command.getTarget(), command.getValue());
                    elseIfCommand.setUuid(commandStack.peek().getUuid());

                    IConditionCommand popElseIf = commandStack.pop();
                    if (!commandStack.empty()) {
                        commandStack.peek().addSubCommand(popElseIf);
                    } else {
                        commands.add(popElseIf);
                    }

                    commandStack.push(elseIfCommand);
                    break;
                case "else":
                    // if the command stack is empty and the latest stack command is not if or elseIf, throw exception
                    if (commandStack.empty() || !("if".equals(commandStack.peek().getCommand()) || "elseIf".equals(commandStack.peek().getCommand()))) {
                        throw new CommandExecuteException("else command must be after if or elseIf command");
                    }
                    ElseCommand elseCommand = new ElseCommand(commandName, command.getTarget(), command.getValue());
                    elseCommand.setUuid(commandStack.peek().getUuid());

                    IConditionCommand popElse = commandStack.pop();
                    if (!commandStack.empty()) {
                        commandStack.peek().addSubCommand(popElse);
                    } else {
                        commands.add(popElse);
                    }
                    commandStack.push(elseCommand);
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
        return switch (commandName) {
            // normal command from selenium ide
            case "addSelection" -> new AddSelectionCommand(commandName, command.getTarget(), command.getValue());
            case "answerOnNextPrompt" ->
                    new AnswerOnNextPromptCommand(commandName, command.getTarget(), command.getValue());
            case "assertAlert", "assertConfirmation", "assertPrompt" ->
                    new AssertAlertCommand(commandName, command.getTarget(), command.getValue());
            case "assertChecked" -> new AssertCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "assert" -> new AssertCommand(commandName, command.getTarget(), command.getValue());
            case "assertEditable" -> new AssertEditableCommand(commandName, command.getTarget(), command.getValue());
            case "assertElementNotPresent" ->
                    new AssertElementNotPresentCommand(commandName, command.getTarget(), command.getValue());
            case "assertElementPresent" ->
                    new AssertElementPresentCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotChecked" ->
                    new AssertNotCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotEditable" ->
                    new AssertNotEditableCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotSelectedLabel" ->
                    new AssertNotSelectedLabelCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotSelectedValue" ->
                    new AssertNotSelectedValueCommand(commandName, command.getTarget(), command.getValue());
            case "assertNotText" -> new AssertNotTextCommand(commandName, command.getTarget(), command.getValue());
            case "assertSelectedLabel" ->
                    new AssertSelectedLabelCommand(commandName, command.getTarget(), command.getValue());
            case "assertSelectedValue" ->
                    new AssertSelectedValueCommand(commandName, command.getTarget(), command.getValue());
            case "assertText" -> new AssertTextCommand(commandName, command.getTarget(), command.getValue());
            case "assertTitle" -> new AssertTitleCommand(commandName, command.getTarget(), command.getValue());
            case "assertValue" -> new AssertValueCommand(commandName, command.getTarget(), command.getValue());
            case "check" -> new CheckCommand(commandName, command.getTarget(), command.getValue());
            case "chooseCancelOnNextConfirmation", "chooseCancelOnNextPrompt" ->
                    new ChooseCancelOnNextConfirmationCommand(commandName, command.getTarget(), command.getValue());
            case "chooseOkOnNextConfirmation", "chooseOkOnNextPrompt" ->
                    new ChooseOkOnNextConfirmationCommand(commandName, command.getTarget(), command.getValue());
            case "clickAt" -> new ClickAtCommand(commandName, command.getTarget(), command.getValue());
            case "click" -> new ClickCommand(commandName, command.getTarget(), command.getValue());
            case "close" -> new CloseCommand(commandName, command.getTarget(), command.getValue());
            case "doubleClickAt" -> new DoubleClickAtCommand(commandName, command.getTarget(), command.getValue());
            case "doubleClick" -> new DoubleClickCommand(commandName, command.getTarget(), command.getValue());
            case "dragAndDropToObject" ->
                    new DragAndDropToObjectCommand(commandName, command.getTarget(), command.getValue());
            case "echo" -> new EchoCommand(commandName, command.getTarget(), command.getValue());
            case "editContent" -> new EditContentCommand(commandName, command.getTarget(), command.getValue());
            case "executeAsyncScript" ->
                    new ExecuteAsyncScriptCommand(commandName, command.getTarget(), command.getValue());
            case "executeScript" -> new ExecuteScriptCommand(commandName, command.getTarget(), command.getValue());
            case "mouseDownAt" -> new MouseDownAtCommand(commandName, command.getTarget(), command.getValue());
            case "mouseDown" -> new MouseDownCommand(commandName, command.getTarget(), command.getValue());
            case "mouseMoveAt" -> new MouseMoveAtCommand(commandName, command.getTarget(), command.getValue());
            case "mouseOut" -> new MouseOutCommand(commandName, command.getTarget(), command.getValue());
            case "mouseOver" -> new MouseOverCommand(commandName, command.getTarget(), command.getValue());
            case "mouseUpAt" -> new MouseUpAtCommand(commandName, command.getTarget(), command.getValue());
            case "mouseUp" -> new MouseUpCommand(commandName, command.getTarget(), command.getValue());
            case "open" -> new OpenCommand(commandName, command.getTarget(), command.getValue());
            case "pause", "wait", "sleep" -> new PauseCommand(commandName, command.getTarget(), command.getValue());
            case "removeSelection" -> new RemoveSelectionCommand(commandName, command.getTarget(), command.getValue());
            case "runCase" -> new RunCaseCommand(commandName, command.getTarget(), command.getValue());
            case "run" -> new RunCommand(commandName, command.getTarget(), command.getValue());
            case "runScript" -> new RunScriptCommand(commandName, command.getTarget(), command.getValue());
            case "screenshot" -> new ScreenshotCommand(commandName, command.getTarget(), command.getValue());
            case "select" -> new SelectCommand(commandName, command.getTarget(), command.getValue());
            case "selectFrame" -> new SelectFrameCommand(commandName, command.getTarget(), command.getValue());
            case "selectWindow" -> new SelectWindowCommand(commandName, command.getTarget(), command.getValue());
            case "sendKeys" -> new SendKeysCommand(commandName, command.getTarget(), command.getValue());
            case "setSpeed" -> new SetSpeedCommand(commandName, command.getTarget(), command.getValue());
            case "setWindowSize" -> new SetWindowSizeCommand(commandName, command.getTarget(), command.getValue());
            case "storeAttribute" -> new StoreAttributeCommand(commandName, command.getTarget(), command.getValue());
            case "store" -> new StoreCommand(commandName, command.getTarget(), command.getValue());
            case "storeJson" -> new StoreJsonCommand(commandName, command.getTarget(), command.getValue());
            case "storeText" -> new StoreTextCommand(commandName, command.getTarget(), command.getValue());
            case "storeTitle" -> new StoreTitleCommand(commandName, command.getTarget(), command.getValue());
            case "storeValue" -> new StoreValueCommand(commandName, command.getTarget(), command.getValue());
            case "storeWindowHandle" ->
                    new StoreWindowHandleCommand(commandName, command.getTarget(), command.getValue());
            case "storeXpathCount" -> new StoreXpathCountCommand(commandName, command.getTarget(), command.getValue());
            case "submit" -> new SubmitCommand(commandName, command.getTarget(), command.getValue());
            case "type" -> new TypeCommand(commandName, command.getTarget(), command.getValue());
            case "uncheck" -> new UncheckCommand(commandName, command.getTarget(), command.getValue());
            case "verifyChecked" -> new VerifyCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "verify" -> new VerifyCommand(commandName, command.getTarget(), command.getValue());
            case "verifyEditable" -> new VerifyEditableCommand(commandName, command.getTarget(), command.getValue());
            case "verifyElementNotPresent" ->
                    new VerifyElementNotPresentCommand(commandName, command.getTarget(), command.getValue());
            case "verifyElementPresent" ->
                    new VerifyElementPresentCommand(commandName, command.getTarget(), command.getValue());
            case "verifyNotChecked" ->
                    new VerifyNotCheckedCommand(commandName, command.getTarget(), command.getValue());
            case "verifyNotEditable" ->
                    new VerifyNotEditableCommand(commandName, command.getTarget(), command.getValue());
            case "verifyNotSelectedLabel" ->
                    new VerifyNotSelectedLabelCommand(commandName, command.getTarget(), command.getValue());
            case "verifyNotSelectedValue" ->
                    new VerifyNotSelectedValueCommand(commandName, command.getTarget(), command.getValue());
            case "verifyNotText" -> new VerifyNotTextCommand(commandName, command.getTarget(), command.getValue());
            case "verifySelectedLabel" ->
                    new VerifySelectedLabelCommand(commandName, command.getTarget(), command.getValue());
            case "verifySelectedValue" ->
                    new VerifySelectedValueCommand(commandName, command.getTarget(), command.getValue());
            case "verifyText" -> new VerifyTextCommand(commandName, command.getTarget(), command.getValue());
            case "verifyTitle" -> new VerifyTitleCommand(commandName, command.getTarget(), command.getValue());
            case "verifyValue" -> new VerifyValueCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementEditable" ->
                    new WaitForElementEditableCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementNotEditable" ->
                    new WaitForElementNotEditableCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementNotPresent" ->
                    new WaitForElementNotPresentCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementNotVisible" ->
                    new WaitForElementNotVisibleCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementPresent" ->
                    new WaitForElementPresentCommand(commandName, command.getTarget(), command.getValue());
            case "waitForElementVisible" ->
                    new WaitForElementVisibleCommand(commandName, command.getTarget(), command.getValue());
            case "waitForText" -> new WaitForTextCommand(commandName, command.getTarget(), command.getValue());
            case "webdriverAnswerOnVisiblePrompt" ->
                    new WebdriverAnswerOnVisiblePromptCommand(commandName, command.getTarget(), command.getValue());
            case "webdriverChooseCancelOnVisibleConfirmation", "webdriverChooseCancelOnVisiblePrompt" ->
                    new WebdriverChooseCancelOnVisibleConfirmationCommand(commandName, command.getTarget(), command.getValue());
            case "webdriverChooseOkOnVisibleConfirmation" ->
                    new WebdriverChooseOkOnVisibleConfirmationCommand(commandName, command.getTarget(), command.getValue());

            // ext command
            default -> parseExtCommand(commandName, command.getTarget(), command.getValue());
        };
    }

    private static ICommand parseExtCommand(String commandName, String target, String value) {
        if (ObjectUtil.isEmpty(EXT_COMMAND_CLASS_MAP)) {
            ClassUtil.scanPackage("io.hugang.execute.ext").forEach(
                    clazz -> {
                        try {
                            ICommand cmdInstance = (ICommand) clazz.getConstructor(String.class, String.class, String.class).newInstance(null, null, null);
                            EXT_COMMAND_CLASS_MAP.put(cmdInstance.getCommand(), clazz);
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException ignored) {
                        }
                    });
        }

        Class<?> clazz = EXT_COMMAND_CLASS_MAP.get(commandName);
        if (ObjectUtil.isNotEmpty(clazz)) {
            try {
                return (ICommand) clazz.getConstructor(String.class, String.class, String.class).newInstance(commandName, target, value);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new AutoTestException(e);
            }
        }
        throw new AutoTestException("command not found: " + commandName);
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
        String baseUrl = StrUtil.EMPTY;
        Object url = json.getByPath("url");
        if (!ObjectUtil.isEmpty(url)) {
            baseUrl = url.toString();
        }
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
