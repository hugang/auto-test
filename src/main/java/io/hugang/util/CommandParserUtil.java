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
import io.hugang.annotation.WebCommand;
import io.hugang.bean.OriginalCommand;
import io.hugang.exceptions.AutoTestException;
import io.hugang.exceptions.CommandExecuteException;
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
import java.lang.reflect.Constructor;
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
        List<Object> commentRow = null;
        if (ObjectUtil.isEmpty(commandRow) || ObjectUtil.isEmpty(commandRow.get(0))) {
            // if the first row is comment line, set the start line number to 1
            startLineNo = 1;
            commentRow = reader.readRow(0);
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
                    // skip blank command
                    if (ObjectUtil.isEmpty(commandRow.get(j))) {
                        continue;
                    }
                    OriginalCommand command = new OriginalCommand();
                    // commentRow
                    assert commentRow != null;
                    if (ObjectUtil.isNotEmpty(commentRow) &&
                            commentRow.size() > j &&
                            ObjectUtil.isNotEmpty(commentRow.get(j))) {
                        command.setDescription(commentRow.get(j).toString());
                    }
                    command.setCommand(commandRow.get(j).toString());
                    if (ObjectUtil.isNotEmpty(targetRow) &&
                            targetRow.size() > j &&
                            ObjectUtil.isNotEmpty(targetRow.get(j))) {
                        command.setTarget(targetRow.get(j).toString());
                    }
                    if (ObjectUtil.isNotEmpty(valueRow) &&
                            valueRow.size() > j &&
                            ObjectUtil.isNotEmpty(valueRow.get(j))) {
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
                    IfCommand ifCommand = new IfCommand(command);
                    ifCommand.setUuid(UUID.randomUUID().toString());
                    commandStack.push(ifCommand);
                    break;
                case "elseIf":
                    // if the command stack is empty and the latest stack command is not if or elseIf, throw exception
                    if (commandStack.empty() || !("if".equals(commandStack.peek().getCommand()) || "elseIf".equals(commandStack.peek().getCommand()))) {
                        throw new CommandExecuteException("elseIf command must be after if or elseIf command");
                    }
                    ElseIfCommand elseIfCommand = new ElseIfCommand(command);
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
                    ElseCommand elseCommand = new ElseCommand(command);
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
                    TimesCommand timesCommand = new TimesCommand(command);
                    commandStack.push(timesCommand);
                    break;
                case "while":
                    WhileCommand whileCommand = new WhileCommand(command);
                    commandStack.push(whileCommand);
                    break;
                case "forEach":
                    ForEachCommand forEachCommand = new ForEachCommand(command);
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
            case "addSelection" -> new AddSelectionCommand(command);
            case "answerOnNextPrompt" -> new AnswerOnNextPromptCommand(command);
            case "assertAlert", "assertConfirmation", "assertPrompt" -> new AssertAlertCommand(command);
            case "assertChecked" -> new AssertCheckedCommand(command);
            case "assert" -> new AssertCommand(command);
            case "assertEditable" -> new AssertEditableCommand(command);
            case "assertElementNotPresent" -> new AssertElementNotPresentCommand(command);
            case "assertElementPresent" -> new AssertElementPresentCommand(command);
            case "assertNotChecked" -> new AssertNotCheckedCommand(command);
            case "assertNotEditable" -> new AssertNotEditableCommand(command);
            case "assertNotSelectedLabel" -> new AssertNotSelectedLabelCommand(command);
            case "assertNotSelectedValue" -> new AssertNotSelectedValueCommand(command);
            case "assertNotText" -> new AssertNotTextCommand(command);
            case "assertSelectedLabel" -> new AssertSelectedLabelCommand(command);
            case "assertSelectedValue" -> new AssertSelectedValueCommand(command);
            case "assertText" -> new AssertTextCommand(command);
            case "assertTitle" -> new AssertTitleCommand(command);
            case "assertValue" -> new AssertValueCommand(command);
            case "check" -> new CheckCommand(command);
            case "chooseCancelOnNextConfirmation", "chooseCancelOnNextPrompt" ->
                    new ChooseCancelOnNextConfirmationCommand(command);
            case "chooseOkOnNextConfirmation", "chooseOkOnNextPrompt" -> new ChooseOkOnNextConfirmationCommand(command);
            case "clickAt" -> new ClickAtCommand(command);
            case "click" -> new ClickCommand(command);
            case "close" -> new CloseCommand(command);
            case "doubleClickAt" -> new DoubleClickAtCommand(command);
            case "doubleClick" -> new DoubleClickCommand(command);
            case "dragAndDropToObject" -> new DragAndDropToObjectCommand(command);
            case "echo" -> new EchoCommand(command);
            case "editContent" -> new EditContentCommand(command);
            case "executeAsyncScript" -> new ExecuteAsyncScriptCommand(command);
            case "executeScript" -> new ExecuteScriptCommand(command);
            case "mouseDownAt" -> new MouseDownAtCommand(command);
            case "mouseDown" -> new MouseDownCommand(command);
            case "mouseMoveAt" -> new MouseMoveAtCommand(command);
            case "mouseOut" -> new MouseOutCommand(command);
            case "mouseOver" -> new MouseOverCommand(command);
            case "mouseUpAt" -> new MouseUpAtCommand(command);
            case "mouseUp" -> new MouseUpCommand(command);
            case "open" -> new OpenCommand(command);
            case "pause", "wait", "sleep" -> new PauseCommand(command);
            case "removeSelection" -> new RemoveSelectionCommand(command);
            case "runCase" -> new RunCaseCommand(command);
            case "run" -> new RunCommand(command);
            case "runScript" -> new RunScriptCommand(command);
            case "screenshot" -> new ScreenshotCommand(command);
            case "select" -> new SelectCommand(command);
            case "selectFrame" -> new SelectFrameCommand(command);
            case "selectWindow" -> new SelectWindowCommand(command);
            case "sendKeys" -> new SendKeysCommand(command);
            case "setSpeed" -> new SetSpeedCommand(command);
            case "setWindowSize" -> new SetWindowSizeCommand(command);
            case "storeAttribute" -> new StoreAttributeCommand(command);
            case "store" -> new StoreCommand(command);
            case "storeJson" -> new StoreJsonCommand(command);
            case "storeText" -> new StoreTextCommand(command);
            case "storeTitle" -> new StoreTitleCommand(command);
            case "storeValue" -> new StoreValueCommand(command);
            case "storeWindowHandle" -> new StoreWindowHandleCommand(command);
            case "storeXpathCount" -> new StoreXpathCountCommand(command);
            case "submit" -> new SubmitCommand(command);
            case "type" -> new TypeCommand(command);
            case "uncheck" -> new UncheckCommand(command);
            case "verifyChecked" -> new VerifyCheckedCommand(command);
            case "verify" -> new VerifyCommand(command);
            case "verifyEditable" -> new VerifyEditableCommand(command);
            case "verifyElementNotPresent" -> new VerifyElementNotPresentCommand(command);
            case "verifyElementPresent" -> new VerifyElementPresentCommand(command);
            case "verifyNotChecked" -> new VerifyNotCheckedCommand(command);
            case "verifyNotEditable" -> new VerifyNotEditableCommand(command);
            case "verifyNotSelectedLabel" -> new VerifyNotSelectedLabelCommand(command);
            case "verifyNotSelectedValue" -> new VerifyNotSelectedValueCommand(command);
            case "verifyNotText" -> new VerifyNotTextCommand(command);
            case "verifySelectedLabel" -> new VerifySelectedLabelCommand(command);
            case "verifySelectedValue" -> new VerifySelectedValueCommand(command);
            case "verifyText" -> new VerifyTextCommand(command);
            case "verifyTitle" -> new VerifyTitleCommand(command);
            case "verifyValue" -> new VerifyValueCommand(command);
            case "waitForElementEditable" -> new WaitForElementEditableCommand(command);
            case "waitForElementNotEditable" -> new WaitForElementNotEditableCommand(command);
            case "waitForElementNotPresent" -> new WaitForElementNotPresentCommand(command);
            case "waitForElementNotVisible" -> new WaitForElementNotVisibleCommand(command);
            case "waitForElementPresent" -> new WaitForElementPresentCommand(command);
            case "waitForElementVisible" -> new WaitForElementVisibleCommand(command);
            case "waitForText" -> new WaitForTextCommand(command);
            case "webdriverAnswerOnVisiblePrompt" -> new WebdriverAnswerOnVisiblePromptCommand(command);
            case "webdriverChooseCancelOnVisibleConfirmation", "webdriverChooseCancelOnVisiblePrompt" ->
                    new WebdriverChooseCancelOnVisibleConfirmationCommand(command);
            case "webdriverChooseOkOnVisibleConfirmation" -> new WebdriverChooseOkOnVisibleConfirmationCommand(command);

            // ext command
            default -> parseExtCommand(command);
        };
    }

    private static ICommand parseExtCommand(OriginalCommand command) {
        if (ObjectUtil.isEmpty(EXT_COMMAND_CLASS_MAP)) {
            Set<Class<?>> classes = ClassUtil.scanPackage("io.hugang.execute.ext");
            classes.forEach(
                    clazz -> {
                        try {
                            // if clazz extends Command
                            if (!Command.class.isAssignableFrom(clazz)) {
                                return;
                            }
                            Constructor<?> constructor = clazz.getConstructor(OriginalCommand.class);
                            ICommand cmdInstance = (ICommand) constructor.newInstance(new OriginalCommand());
                            EXT_COMMAND_CLASS_MAP.put(cmdInstance.getCommand(), clazz);
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 IllegalAccessException ignored) {
                            log.error("parse ext command error: {}", clazz.getName());
                        }
                    });
        }

        Class<?> clazz = EXT_COMMAND_CLASS_MAP.get(command.getCommand());
        if (ObjectUtil.isNotEmpty(clazz)) {
            try {
                return (ICommand) clazz.getConstructor(OriginalCommand.class).newInstance(command);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new AutoTestException(e);
            }
        }
        throw new AutoTestException("command not found: " + command.getCommand());
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
                // description
                if (ObjectUtil.isNotEmpty(jsonCommand.getByPath("description"))) {
                    command.setDescription(jsonCommand.getByPath("description").toString());
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

            boolean hasDescription = false;
            for (ICommand command : commands.getCommands()) {
                Command c1 = (Command) command;
                if (StrUtil.isNotEmpty(c1.getDescription())) {
                    hasDescription = true;
                    break;
                }
            }

            int beginRow = hasDescription ? 1 : 0;
            writer.writeCellValue(0, beginRow, "TestCase");
            writer.writeCellValue(0, beginRow + 2, i + 1);

            for (int i1 = 0; i1 < commands.getCommands().size(); i1++) {
                Command command = (Command) commands.getCommands().get(i1);
                if (hasDescription) {
                    writer.writeCellValue(i1 + 1, 0, command.getDescription());
                }
                // write command to first row
                writer.writeCellValue(i1 + 1, beginRow, command.getCommand());
                // write target to second row
                writer.writeCellValue(i1 + 1, beginRow + 1, command.getTarget());
                // write value to third row
                writer.writeCellValue(i1 + 1, beginRow + 2, command.getValue());
            }
            writer.flush();
            writer.close();
        }
    }
}
