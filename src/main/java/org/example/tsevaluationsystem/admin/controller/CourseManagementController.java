package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.CourseManagementService;
import org.example.tsevaluationsystem.dto.Course;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/admin/course")
@RestController
public class CourseManagementController {

    @Autowired
    private CourseManagementService courseManagementService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Course course) {
        return courseManagementService.insert(course);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return courseManagementService.list(params);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Course course) {
        return courseManagementService.update(course);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return courseManagementService.delete(params.get("id"));
    }

    @PostMapping("/all")
    public Result all() {
        return courseManagementService.listAll();
    }
}
