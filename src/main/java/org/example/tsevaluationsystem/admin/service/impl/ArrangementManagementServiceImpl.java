package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.ArrangementManagementMapper;
import org.example.tsevaluationsystem.admin.service.ArrangementManagementService;
import org.example.tsevaluationsystem.dto.ClassTeacher;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArrangementManagementServiceImpl implements ArrangementManagementService {

    @Autowired
    private ArrangementManagementMapper arrangementManagementMapper;

    @Override
    public Result insert(ClassTeacher classTeacher) {
        classTeacher.setId(IdWorker.getId());
        arrangementManagementMapper.insert(classTeacher);
        return new Result(1, "success", null);
    }

    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<ClassTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("classId") != null && !params.get("classId").toString().isEmpty()) {
            wrapper.eq("class_id", Long.parseLong(params.get("classId").toString()));
        }
        if (params.get("teacherId") != null && !params.get("teacherId").toString().isEmpty()) {
            wrapper.eq("teacher_id", Long.parseLong(params.get("teacherId").toString()));
        }
        if (params.get("courseId") != null && !params.get("courseId").toString().isEmpty()) {
            wrapper.eq("course_id", Long.parseLong(params.get("courseId").toString()));
        }
        if (params.get("semester") != null && !params.get("semester").toString().isEmpty()) {
            wrapper.eq("semester", params.get("semester").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<ClassTeacher> list = arrangementManagementMapper.selectList(wrapper);
        PageInfo<ClassTeacher> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    @Override
    public Result update(ClassTeacher classTeacher) {
        arrangementManagementMapper.updateById(classTeacher);
        return new Result(1, "success", null);
    }

    @Override
    public Result delete(Long id) {
        ClassTeacher classTeacher = arrangementManagementMapper.selectById(id);
        if (classTeacher == null) {
            return new Result(0, "排课记录不存在", null);
        }
        classTeacher.setIsDele(1);
        arrangementManagementMapper.updateById(classTeacher);
        return new Result(1, "success", null);
    }
}
