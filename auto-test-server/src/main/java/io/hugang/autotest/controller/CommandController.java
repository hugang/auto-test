package io.hugang.autotest.controller;

import io.hugang.autotest.entity.Command;
import io.hugang.autotest.repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commands")
public class CommandController {
    @Autowired
    private CommandRepository commandRepository;

    @GetMapping
    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    @GetMapping("/{id}")
    public Command getCommandById(@PathVariable Long id) {
        return commandRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Command createCommand(@RequestBody Command command) {
        return commandRepository.save(command);
    }

    @PutMapping("/{id}")
    public Command updateCommand(@PathVariable Long id, @RequestBody Command command) {
        Command existingCommand = commandRepository.findById(id).orElse(null);
        if (existingCommand != null) {
            existingCommand.setCommand(command.getCommand());
            existingCommand.setScriptId(command.getScriptId());
            existingCommand.setTarget(command.getTarget());
            existingCommand.setValue(command.getValue());
            existingCommand.setDescription(command.getDescription());
            return commandRepository.save(existingCommand);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCommand(@PathVariable Long id) {
        commandRepository.deleteById(id);
    }
}
