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

/**
 * 班级管理控制器
 * <p>负责班级信息的增删改查及班级学生管理</p>
 */
@RequestMapping("/admin/class")
@RestController
public class ClassManagementController {

    @Autowired
    private ClassManagementService classManagementService;

    /**
     * 新增班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody ClassInfo classInfo) {
        return classManagementService.insert(classInfo);
    }

    /**
     * 查询班级列表（分页）
     * @param params 查询参数
     * @return 班级列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return classManagementService.list(params);
    }

    /**
     * 编辑班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody ClassInfo classInfo) {
        return classManagementService.update(classInfo);
    }

    /**
     * 删除班级（逻辑删除）
     * @param params 包含id的参数
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return classManagementService.delete(params.get("id"));
    }

    /**
     * 查询所有班级
     * @return 班级列表
     */
    @PostMapping("/all")
    public Result all() {
        return classManagementService.listAll();
    }

    /**
     * 查询班级学生列表
     * @param params 包含classId的参数
     * @return 学生列表
     */
    @PostMapping("/student/list")
    public Result listClassStudents(@RequestBody Map<String, Long> params) {
        return classManagementService.listClassStudents(params.get("classId"));
    }

    /**
     * 查询未分配班级的学生
     * @return 未分配学生列表
     */
    @PostMapping("/student/unassigned")
    public Result listUnassignedStudents() {
        return classManagementService.listUnassignedStudents();
    }

    /**
     * 将学生加入班级
     * @param params 包含classId和studentId的参数
     * @return 操作结果
     */
    @PostMapping("/student/add")
    public Result addStudentToClass(@RequestBody Map<String, Long> params) {
        return classManagementService.addStudentToClass(params.get("classId"), params.get("studentId"));
    }

    /**
     * 将学生移出班级
     * @param params 包含id的参数
     * @return 操作结果
     */
    @PostMapping("/student/remove")
    public Result removeStudentFromClass(@RequestBody Map<String, Long> params) {
        return classManagementService.removeStudentFromClass(params.get("id"));
    }
}
