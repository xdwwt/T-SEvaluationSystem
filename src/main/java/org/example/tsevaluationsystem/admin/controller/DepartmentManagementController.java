package org.example.tsevaluationsystem.admin.controller;

import org.example.tsevaluationsystem.admin.service.DepartmentManagementService;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/department")
public class DepartmentManagementController {

    @Autowired
    private DepartmentManagementService departmentManagementService;

    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        if (department.getDeptName() == null || department.getDeptName().trim().isEmpty()) {
            return new Result(0, "院系名称不能为空", null);
        }
        department.setDeptName(department.getDeptName().trim());
        return departmentManagementService.insert(department);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Object> params) {
        return departmentManagementService.list(params);
    }

    @PostMapping("/listAll")
    public Result listAll() {
        return departmentManagementService.listAll();
    }

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

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return departmentManagementService.delete(id);
    }
}
