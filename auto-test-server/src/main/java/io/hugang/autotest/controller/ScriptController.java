package io.hugang.autotest.controller;

import io.hugang.autotest.entity.Script;
import io.hugang.autotest.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scripts")
public class ScriptController {
    @Autowired
    private ScriptRepository scriptRepository;

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

}
