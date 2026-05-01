package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.MajorManagementService;
import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/major")
public class MajorManagementController {

    @Autowired
    private MajorManagementService majorManagementService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Major major) {
        return majorManagementService.insert(major);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return majorManagementService.list(params);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Major major) {
        return majorManagementService.update(major);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return majorManagementService.delete(params.get("id"));
    }

    @PostMapping("/all")
    public Result all(@RequestBody(required = false) Map<String, Object> params) {
        return majorManagementService.listAll(params);
    }
}
