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

/**
 * 专业管理控制器
 * <p>负责专业信息的增删改查</p>
 */
@RestController
@RequestMapping("/admin/major")
public class MajorManagementController {

    @Autowired
    private MajorManagementService majorManagementService;

    /**
     * 新增专业
     * @param major 专业信息
     * @return 操作结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody Major major) {
        return majorManagementService.insert(major);
    }

    /**
     * 查询专业列表（分页）
     * @param params 查询参数
     * @return 专业列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return majorManagementService.list(params);
    }

    /**
     * 编辑专业
     * @param major 专业信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Major major) {
        return majorManagementService.update(major);
    }

    /**
     * 删除专业（逻辑删除）
     * @param params 包含id的参数
     * @return 操作结果
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map<String, Long> params) {
        return majorManagementService.delete(params.get("id"));
    }

    /**
     * 查询所有专业
     * @param params 可选查询参数（如departmentId）
     * @return 专业列表
     */
    @PostMapping("/all")
    public Result all(@RequestBody(required = false) Map<String, Object> params) {
        return majorManagementService.listAll(params);
    }
}
