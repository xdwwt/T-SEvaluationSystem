package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.DepartmentManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.MajorManagementMapper;
import org.example.tsevaluationsystem.admin.service.MajorManagementService;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 专业管理服务实现类
 */
@Service
public class MajorManagementServiceImpl implements MajorManagementService {

    @Autowired
    private MajorManagementMapper majorManagementMapper;
    @Autowired
    private DepartmentManagementMapper departmentManagementMapper;

    /**
     * 新增专业
     * @param major 专业信息
     * @return 操作结果
     */
    @Override
    public Result insert(Major major) {
        if (major.getDepartmentId() == null) {
            return new Result(0, "所属院系不能为空", null);
        }
        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        wrapper.eq("major_name", major.getMajorName());
        if (majorManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "专业名称已存在", null);
        }
        major.setId(IdWorker.getId());
        major.setIsDele(0);
        majorManagementMapper.insert(major);
        return new Result(1, "success", null);
    }

    /**
     * 查询专业列表（分页）
     * @param params 查询参数
     * @return 专业列表
     */
    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("majorName") != null && !params.get("majorName").toString().isEmpty()) {
            wrapper.like("major_name", params.get("majorName").toString());
        }
        if (params.get("deptName") != null && !params.get("deptName").toString().isEmpty()) {
            QueryWrapper<Department> deptWrapper = new QueryWrapper<>();
            deptWrapper.like("dept_name", params.get("deptName").toString());
            deptWrapper.eq("is_dele", 0);
            List<Department> deptList = departmentManagementMapper.selectList(deptWrapper);
            List<Long> deptIds = deptList.stream().map(Department::getId).collect(Collectors.toList());
            if (!deptIds.isEmpty()) {
                wrapper.in("department_id", deptIds);
            } else {
                wrapper.eq("department_id", -1L);
            }
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<Major> list = majorManagementMapper.selectList(wrapper);
        PageInfo<Major> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    /**
     * 编辑专业
     * @param major 专业信息
     * @return 操作结果
     */
    @Override
    public Result update(Major major) {
        Major exist = majorManagementMapper.selectById(major.getId());
        if (exist == null) {
            return new Result(0, "专业不存在", null);
        }
        majorManagementMapper.updateById(major);
        return new Result(1, "success", null);
    }

    /**
     * 删除专业（逻辑删除）
     * @param id 专业ID
     * @return 操作结果
     */
    @Override
    public Result delete(Long id) {
        Major major = majorManagementMapper.selectById(id);
        if (major == null) {
            return new Result(0, "专业不存在", null);
        }
        major.setIsDele(1);
        majorManagementMapper.updateById(major);
        return new Result(1, "success", null);
    }

    /**
     * 查询所有专业
     * @param params 可选查询参数
     * @return 专业列表
     */
    @Override
    public Result listAll(Map<String, Object> params) {
        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        if (params != null && params.get("departmentId") != null && !params.get("departmentId").toString().isEmpty()) {
            wrapper.eq("department_id", params.get("departmentId").toString());
        }
        wrapper.orderByAsc("major_name");
        List<Major> list = majorManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }
}
