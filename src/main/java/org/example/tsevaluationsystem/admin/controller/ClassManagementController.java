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

    @PostMapping("/all")
    public Result all() {
        return classManagementService.listAll();
    }

    @PostMapping("/student/list")
    public Result listClassStudents(@RequestBody Map<String, Long> params) {
        return classManagementService.listClassStudents(params.get("classId"));
    }

    @PostMapping("/student/unassigned")
    public Result listUnassignedStudents() {
        return classManagementService.listUnassignedStudents();
    }

    @PostMapping("/student/add")
    public Result addStudentToClass(@RequestBody Map<String, Long> params) {
        return classManagementService.addStudentToClass(params.get("classId"), params.get("studentId"));
    }

    @PostMapping("/student/remove")
    public Result removeStudentFromClass(@RequestBody Map<String, Long> params) {
        return classManagementService.removeStudentFromClass(params.get("id"));
    }
}
