package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.name}Service;

    @GetMapping(value = "")
    public Object list() {
        List<${table.entityName}> ${table.name}s = ${table.name}Service.list();
        Map<String, Object> result = new HashMap<>();
        result.put("data", ${table.name}s);
        result.put("status", 200);
        result.put("message", "OK");
        return result;
    }

    @GetMapping(value = "/{id}")
    public Object get(@PathVariable("id") int id) {
        ${table.entityName} ${table.name} = ${table.name}Service.getById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", ${table.name});
        result.put("status", 200);
        result.put("message", "OK");
        return result;
    }

    @PostMapping(value = "")
    public Object post(@RequestBody ${table.entityName} ${table.name}) {
        Map<String, Object> result = new HashMap<>();
        try {
            ${table.name}Service.save(${table.name});
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }

    @PutMapping(value = "")
    public Object put(@RequestBody ${table.entityName} ${table.name}) {
        Map<String, Object> result = new HashMap<>();
        try {
            ${table.name}Service.updateById(${table.name});
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }

    @DeleteMapping(value = "")
    public Object delete(@RequestBody ${table.entityName} ${table.name}) {
        Map<String, Object> result = new HashMap<>();
        try {
            ${table.name}Service.removeById(${table.name}.getId());
            result.put("status", 200);
            result.put("message", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("message", "ERROR");
        }
        return result;
    }
}
</#if>
