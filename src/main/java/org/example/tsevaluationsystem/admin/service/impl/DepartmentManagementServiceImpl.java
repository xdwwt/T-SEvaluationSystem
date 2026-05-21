package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.DepartmentManagementMapper;
import org.example.tsevaluationsystem.admin.service.DepartmentManagementService;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 院系管理服务实现类
 */
@Service
public class DepartmentManagementServiceImpl implements DepartmentManagementService {

    @Autowired
    private DepartmentManagementMapper departmentManagementMapper;

    /**
     * 新增院系
     * @param department 院系信息
     * @return 操作结果
     */
    @Override
    public Result insert(Department department) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("dept_name", department.getDeptName());
        if (departmentManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "院系名称已存在", null);
        }

        department.setId(IdWorker.getId());
        departmentManagementMapper.insert(department);
        return new Result(1, "success", department.getId());
    }

    /**
     * 查询院系列表（分页）
     * @param params 查询参数
     * @return 院系列表
     */
    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("deptName") != null && !params.get("deptName").toString().isEmpty()) {
            wrapper.like("dept_name", params.get("deptName").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentManagementMapper.selectList(wrapper);
        PageInfo<Department> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    /**
     * 查询所有院系
     * @return 院系列表
     */
    @Override
    public Result listAll() {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        wrapper.orderByAsc("dept_name");
        List<Department> list = departmentManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }

    /**
     * 编辑院系
     * @param department 院系信息
     * @return 操作结果
     */
    @Override
    public Result update(Department department) {
        Department exist = departmentManagementMapper.selectById(department.getId());
        if (exist == null) {
            return new Result(0, "院系不存在", null);
        }

        // 校验名称是否与其他院系重复
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("dept_name", department.getDeptName());
        wrapper.ne("id", department.getId());
        if (departmentManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "院系名称已存在", null);
        }

        departmentManagementMapper.updateById(department);
        return new Result(1, "success", null);
    }

    /**
     * 删除院系（逻辑删除）
     * @param id 院系ID
     * @return 操作结果
     */
    @Override
    public Result delete(Long id) {
        Department exist = departmentManagementMapper.selectById(id);
        if (exist == null) {
            return new Result(0, "院系不存在", null);
        }

        exist.setIsDele(1);
        departmentManagementMapper.updateById(exist);
        return new Result(1, "success", null);
    }
}
