package org.example.tsevaluationsystem.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.tsevaluationsystem.admin.mapper.*;
import org.example.tsevaluationsystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private TeacherManagementMapper teacherManagementMapper;
    @Autowired
    private StudentManagementMapper studentManagementMapper;
    @Autowired
    private ClassManagementMapper classManagementMapper;
    @Autowired
    private CourseManagementMapper courseManagementMapper;
    @Autowired
    private ArrangementManagementMapper arrangementManagementMapper;
    @Autowired
    private MajorManagementMapper majorManagementMapper;
    @Autowired
    private DepartmentManagementMapper departmentManagementMapper;
    @Autowired
    private TitleManagementMapper titleManagementMapper;

    @PostMapping("/stats")
    public Result stats() {
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<TeacherInfo> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.eq("is_dele", 0);
        data.put("teacherCount", teacherManagementMapper.selectCount(teacherWrapper));

        QueryWrapper<StudentInfo> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("is_dele", 0);
        data.put("studentCount", studentManagementMapper.selectCount(studentWrapper));

        QueryWrapper<ClassInfo> classWrapper = new QueryWrapper<>();
        classWrapper.eq("is_dele", 0);
        data.put("classCount", classManagementMapper.selectCount(classWrapper));

        QueryWrapper<Course> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("is_dele", 0);
        data.put("courseCount", courseManagementMapper.selectCount(courseWrapper));

        QueryWrapper<ClassTeacher> arrangementWrapper = new QueryWrapper<>();
        arrangementWrapper.eq("is_dele", 0);
        data.put("arrangementCount", arrangementManagementMapper.selectCount(arrangementWrapper));

        QueryWrapper<Major> majorWrapper = new QueryWrapper<>();
        majorWrapper.eq("is_dele", 0);
        data.put("majorCount", majorManagementMapper.selectCount(majorWrapper));

        QueryWrapper<Department> departmentWrapper = new QueryWrapper<>();
        departmentWrapper.eq("is_dele", 0);
        data.put("departmentCount", departmentManagementMapper.selectCount(departmentWrapper));

        QueryWrapper<Title> titleWrapper = new QueryWrapper<>();
        titleWrapper.eq("is_dele", 0);
        data.put("titleCount", titleManagementMapper.selectCount(titleWrapper));

        return new Result(1, "success", data);
    }
}
