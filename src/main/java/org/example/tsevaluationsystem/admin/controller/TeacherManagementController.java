package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.TeacherManagementService;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 教师管理
 */
@RequestMapping("/admin/teacher")
@RestController
public class TeacherManagementController {

    @Autowired
    private TeacherManagementService teacherManagementService;

    /**
     * 添加教师
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody TeacherInfo teacherInfo){
        return teacherManagementService.insert(teacherInfo);
    }

    /**
     * 查询教师列表（分页）
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params){
        return teacherManagementService.list(params);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, String> params){
        return teacherManagementService.resetPassword(params.get("userId"));
    }
}
