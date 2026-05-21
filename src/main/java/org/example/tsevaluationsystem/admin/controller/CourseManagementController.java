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

/**
 * 课程管理控制器
 * <p>负责课程信息的增删改查</p>
 */
@RequestMapping("/admin/course")
@RestController
public class CourseManagementController {

    @Autowired
    private CourseManagementService courseManagementService;

    /**
     * 新增课程
     * @param course 课程信息
     * @return 操作结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Course course) {
        return courseManagementService.insert(course);
    }

    /**
     * 查询课程列表（分页）
     * @param params 查询参数
     * @return 课程列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return courseManagementService.list(params);
    }

    /**
     * 编辑课程
     * @param course 课程信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Course course) {
        return courseManagementService.update(course);
    }

    /**
     * 删除课程（逻辑删除）
     * @param params 包含id的参数
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return courseManagementService.delete(params.get("id"));
    }

    /**
     * 查询所有课程
     * @return 课程列表
     */
    @PostMapping("/all")
    public Result all() {
        return courseManagementService.listAll();
    }
}
