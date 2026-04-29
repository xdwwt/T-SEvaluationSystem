package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.ArrangementManagementService;
import org.example.tsevaluationsystem.dto.ClassTeacher;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/admin/arrangement")
@RestController
public class ArrangementManagementController {

    @Autowired
    private ArrangementManagementService arrangementManagementService;

    @PostMapping("/insert")
    public Result insert(@RequestBody ClassTeacher classTeacher) {
        return arrangementManagementService.insert(classTeacher);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return arrangementManagementService.list(params);
    }

    @PostMapping("/update")
    public Result update(@RequestBody ClassTeacher classTeacher) {
        return arrangementManagementService.update(classTeacher);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return arrangementManagementService.delete(params.get("id"));
    }
}
