package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.ArrangementManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.ClassManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.CourseManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.TeacherManagementMapper;
import org.example.tsevaluationsystem.admin.service.ArrangementManagementService;
import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.ClassTeacher;
import org.example.tsevaluationsystem.dto.Course;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.TeacherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArrangementManagementServiceImpl implements ArrangementManagementService {

    @Autowired
    private ArrangementManagementMapper arrangementManagementMapper;
    @Autowired
    private CourseManagementMapper courseManagementMapper;
    @Autowired
    private ClassManagementMapper classManagementMapper;
    @Autowired
    private TeacherManagementMapper teacherManagementMapper;

    @Override
    public Result insert(ClassTeacher classTeacher) {
        QueryWrapper<ClassTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", classTeacher.getClassId())
               .eq("teacher_id", classTeacher.getTeacherId())
               .eq("course_id", classTeacher.getCourseId())
               .eq("semester", classTeacher.getSemester())
               .eq("is_dele", 0);
        if (arrangementManagementMapper.selectCount(wrapper) > 0) {
            return new Result(0, "该排课记录已存在", null);
        }

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

        if (params.get("className") != null && !params.get("className").toString().isEmpty()) {
            QueryWrapper<ClassInfo> classWrapper = new QueryWrapper<>();
            classWrapper.like("class_name", params.get("className").toString());
            classWrapper.eq("is_dele", 0);
            List<ClassInfo> classList = classManagementMapper.selectList(classWrapper);
            List<Long> classIds = classList.stream().map(ClassInfo::getId).collect(Collectors.toList());
            if (!classIds.isEmpty()) {
                wrapper.in("class_id", classIds);
            } else {
                wrapper.eq("class_id", -1L);
            }
        }
        if (params.get("teacherName") != null && !params.get("teacherName").toString().isEmpty()) {
            QueryWrapper<TeacherInfo> teacherWrapper = new QueryWrapper<>();
            teacherWrapper.like("name", params.get("teacherName").toString());
            teacherWrapper.eq("is_dele", 0);
            List<TeacherInfo> teacherList = teacherManagementMapper.selectList(teacherWrapper);
            List<Long> teacherIds = teacherList.stream().map(TeacherInfo::getId).collect(Collectors.toList());
            if (!teacherIds.isEmpty()) {
                wrapper.in("teacher_id", teacherIds);
            } else {
                wrapper.eq("teacher_id", -1L);
            }
        }
        if (params.get("courseName") != null && !params.get("courseName").toString().isEmpty()) {
            QueryWrapper<Course> courseWrapper = new QueryWrapper<>();
            courseWrapper.like("course_name", params.get("courseName").toString());
            courseWrapper.eq("is_dele", 0);
            List<Course> courseList = courseManagementMapper.selectList(courseWrapper);
            List<Long> courseIds = courseList.stream().map(Course::getId).collect(Collectors.toList());
            if (!courseIds.isEmpty()) {
                wrapper.in("course_id", courseIds);
            } else {
                wrapper.eq("course_id", -1L);
            }
        }
        if (params.get("semester") != null && !params.get("semester").toString().isEmpty()) {
            wrapper.eq("semester", params.get("semester").toString());
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<ClassTeacher> list = arrangementManagementMapper.selectList(wrapper);
        PageInfo<ClassTeacher> pageInfo = new PageInfo<>(list);

        // 填充名称
        if (!list.isEmpty()) {
            List<Long> classIds = list.stream().map(ClassTeacher::getClassId).distinct().collect(Collectors.toList());
            List<Long> teacherIds = list.stream().map(ClassTeacher::getTeacherId).distinct().collect(Collectors.toList());
            List<Long> courseIds = list.stream().map(ClassTeacher::getCourseId).distinct().collect(Collectors.toList());

            Map<Long, String> classNameMap = classManagementMapper.selectBatchIds(classIds)
                    .stream().collect(Collectors.toMap(ClassInfo::getId, ClassInfo::getClassName));
            Map<Long, String> teacherNameMap = teacherManagementMapper.selectBatchIds(teacherIds)
                    .stream().collect(Collectors.toMap(TeacherInfo::getId, TeacherInfo::getName));
            Map<Long, String> courseNameMap = courseManagementMapper.selectBatchIds(courseIds)
                    .stream().collect(Collectors.toMap(Course::getId, Course::getCourseName));

            for (ClassTeacher ct : list) {
                ct.setClassName(classNameMap.getOrDefault(ct.getClassId(), ""));
                ct.setTeacherName(teacherNameMap.getOrDefault(ct.getTeacherId(), ""));
                ct.setCourseName(courseNameMap.getOrDefault(ct.getCourseId(), ""));
            }
        }

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
