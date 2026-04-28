package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.StudentManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 学生管理
 */
@RequestMapping("/admin/student")
@RestController
public class StudentManagementController {

    @Autowired
    private StudentManagementService studentManagementService;

    /**
     * 添加学生
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody StudentInfo studentInfo){
        return studentManagementService.insert(studentInfo);
    }

    /**
     * 查询学生列表（分页）
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params){
        return studentManagementService.list(params);
    }

    /**
     * 编辑学生
     */
    @PostMapping("/update")
    public Result update(@RequestBody StudentInfo studentInfo){
        return studentManagementService.update(studentInfo);
    }

    /**
     * 删除学生（逻辑删除）
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, String> params){
        return studentManagementService.delete(params.get("userId"));
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, String> params){
        return studentManagementService.resetPassword(params.get("userId"));
    }
}
