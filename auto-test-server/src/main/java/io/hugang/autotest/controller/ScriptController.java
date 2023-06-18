package io.hugang.autotest.controller;

import io.hugang.BasicExecutor;
import io.hugang.autotest.entity.Command;
import io.hugang.autotest.entity.Script;
import io.hugang.autotest.repository.CommandRepository;
import io.hugang.autotest.repository.ScriptRepository;
import io.hugang.bean.Commands;
import io.hugang.bean.OriginalCommand;
import io.hugang.parse.CommandParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scripts")
public class ScriptController {
    @Autowired
    private ScriptRepository scriptRepository;
    @Autowired
    private CommandRepository commandRepository;

    @GetMapping
    public List<Script> getAllScripts() {
        return scriptRepository.findAll();
    }

    @GetMapping("/{id}")
    public Script getScriptById(@PathVariable("id") int id) {
        return scriptRepository.findById((long) id).orElse(null);
    }

    @PostMapping
    public Script createScript(@RequestBody Script script) {
        return scriptRepository.save(script);
    }

    @PutMapping("/{id}")
    public Script updateScript(@PathVariable("id") int id, @RequestBody Script script) {
        Script existingScript = scriptRepository.findById((long) id).orElse(null);
        if (existingScript != null) {
            existingScript.setScriptName(script.getScriptName());
            return scriptRepository.save(existingScript);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteScript(@PathVariable("id") int id) {
        scriptRepository.deleteById((long) id);
    }

    @PostMapping("/execute/{id}")
    public void executeScript(@PathVariable("id") int id) {
        Script existingScript = scriptRepository.findById((long) id).orElse(null);
        if (existingScript != null) {
            System.out.println("Executing script: " + existingScript.getScriptName());
        } else {
            System.out.println("Script not found");
        }

        // find command by script id
        List<Command> commands = commandRepository.findAll().stream().filter(command -> command.getScriptId().equals(String.valueOf(id)))
                .collect(Collectors.toList());

        BasicExecutor executor = new BasicExecutor();
        List<OriginalCommand> originalCommands = new ArrayList<>();
        List<Commands> commandsList = new ArrayList<>();
        for (Command command : commands) {
            originalCommands.add(new OriginalCommand(command.getCommand(), command.getTarget(), command.getValue()));
        }
        Commands commandList = new Commands();
        commandList.setCommands(CommandParserUtil.parseCommandToSubCommand(originalCommands));
        commandsList.add(commandList);
        executor.runCommandsList(commandsList);
    }

}
