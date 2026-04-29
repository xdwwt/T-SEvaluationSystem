package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.ClassManagementService;
import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/admin/class")
@RestController
public class ClassManagementController {

    @Autowired
    private ClassManagementService classManagementService;

    @PostMapping("/insert")
    public Result insert(@RequestBody ClassInfo classInfo) {
        return classManagementService.insert(classInfo);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return classManagementService.list(params);
    }

    @PostMapping("/update")
    public Result update(@RequestBody ClassInfo classInfo) {
        return classManagementService.update(classInfo);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return classManagementService.delete(params.get("id"));
    }
}
