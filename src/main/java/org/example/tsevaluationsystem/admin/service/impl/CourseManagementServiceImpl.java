package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.CourseManagementMapper;
import org.example.tsevaluationsystem.admin.service.CourseManagementService;
import org.example.tsevaluationsystem.dto.Course;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseManagementServiceImpl implements CourseManagementService {

    @Autowired
    private CourseManagementMapper courseManagementMapper;

    @Override
    public Result insert(Course course) {
        course.setId(IdWorker.getId());
        courseManagementMapper.insert(course);
        return new Result(1, "success", null);
    }

    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("courseCode") != null && !params.get("courseCode").toString().isEmpty()) {
            wrapper.like("course_code", params.get("courseCode").toString());
        }
        if (params.get("courseName") != null && !params.get("courseName").toString().isEmpty()) {
            wrapper.like("course_name", params.get("courseName").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseManagementMapper.selectList(wrapper);
        PageInfo<Course> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    @Override
    public Result update(Course course) {
        courseManagementMapper.updateById(course);
        return new Result(1, "success", null);
    }

    @Override
    public Result listAll() {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        wrapper.orderByAsc("course_name");
        List<Course> list = courseManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }

    @Override
    public Result delete(Long id) {
        Course course = courseManagementMapper.selectById(id);
        if (course == null) {
            return new Result(0, "课程不存在", null);
        }
        course.setIsDele(1);
        courseManagementMapper.updateById(course);
        return new Result(1, "success", null);
    }
}
