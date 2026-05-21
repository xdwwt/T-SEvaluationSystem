package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.DepartmentManagementService;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 院系管理控制器
 * <p>负责院系信息的增删改查</p>
 */
@RestController
@RequestMapping("/admin/department")
public class DepartmentManagementController {

    @Autowired
    private DepartmentManagementService departmentManagementService;

    /**
     * 新增院系
     * @param department 院系信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        if (department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            return new Result(0, "院系名称不能为空", null);
        }
        department.setDeptName(department.getDeptName().trim());
        return departmentManagementService.insert(department);
    }

    /**
     * 查询院系列表（分页）
     * @param params 查询参数
     * @return 院系列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return departmentManagementService.list(params);
    }

    /**
     * 查询所有院系
     * @return 院系列表
     */
    @PostMapping("/listAll")
    public Result listAll() {
        return departmentManagementService.listAll();
    }

    /**
     * 编辑院系
     * @param department 院系信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody Department department) {
        if (department.getId() == null) {
            return new Result(0, "院系ID不能为空", null);
        }
        if (department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            return new Result(0, "院系名称不能为空", null);
        }
        department.setDeptName(department.getDeptName().trim());
        return departmentManagementService.update(department);
    }

    /**
     * 删除院系（逻辑删除）
     * @param id 院系ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return departmentManagementService.delete(id);
    }
}
