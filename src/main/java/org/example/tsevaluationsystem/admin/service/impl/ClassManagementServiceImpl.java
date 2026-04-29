package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.ClassManagementMapper;
import org.example.tsevaluationsystem.admin.service.ClassManagementService;
import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    @Autowired
    private ClassManagementMapper classManagementMapper;

    @Override
    public Result insert(ClassInfo classInfo) {
        classInfo.setId(IdWorker.getId());
        classManagementMapper.insert(classInfo);
        return new Result(1, "success", null);
    }

    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<ClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("className") != null && !params.get("className").toString().isEmpty()) {
            wrapper.like("class_name", params.get("className").toString());
        }
        if (params.get("grade") != null && !params.get("grade").toString().isEmpty()) {
            wrapper.eq("grade", params.get("grade").toString());
        }
        if (params.get("major") != null && !params.get("major").toString().isEmpty()) {
            wrapper.eq("major", params.get("major").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<ClassInfo> list = classManagementMapper.selectList(wrapper);
        PageInfo<ClassInfo> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    @Override
    public Result update(ClassInfo classInfo) {
        classManagementMapper.updateById(classInfo);
        return new Result(1, "success", null);
    }

    @Override
    public Result delete(Long id) {
        ClassInfo classInfo = classManagementMapper.selectById(id);
        if (classInfo == null) {
            return new Result(0, "班级不存在", null);
        }
        classInfo.setIsDele(1);
        classManagementMapper.updateById(classInfo);
        return new Result(1, "success", null);
    }
}
