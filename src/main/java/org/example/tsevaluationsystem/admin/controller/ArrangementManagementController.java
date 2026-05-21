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

/**
 * 排课管理控制器
 * <p>负责班级-教师-课程关联关系的增删改查</p>
 */
@RequestMapping("/admin/arrangement")
@RestController
public class ArrangementManagementController {

    @Autowired
    private ArrangementManagementService arrangementManagementService;

    /**
     * 新增排课记录
     * @param classTeacher 排课信息
     * @return 操作结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody ClassTeacher classTeacher) {
        return arrangementManagementService.insert(classTeacher);
    }

    /**
     * 查询排课列表（分页）
     * @param params 查询参数
     * @return 排课列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return arrangementManagementService.list(params);
    }

    /**
     * 编辑排课记录
     * @param classTeacher 排课信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody ClassTeacher classTeacher) {
        return arrangementManagementService.update(classTeacher);
    }

    /**
     * 删除排课记录（逻辑删除）
     * @param params 包含id的参数
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return arrangementManagementService.delete(params.get("id"));
    }
}
