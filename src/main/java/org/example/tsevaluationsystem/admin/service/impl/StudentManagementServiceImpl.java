package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.ClassManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.ClassStudentMapper;
import org.example.tsevaluationsystem.admin.mapper.MajorManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.StudentManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.UserMapper;
import org.example.tsevaluationsystem.admin.service.StudentManagementService;
import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.ClassStudent;
import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.StudentInfo;
import org.example.tsevaluationsystem.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentManagementServiceImpl implements StudentManagementService {

    @Autowired
    private StudentManagementMapper studentManagementMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassStudentMapper classStudentMapper;
    @Autowired
    private MajorManagementMapper majorManagementMapper;
    @Autowired
    private ClassManagementMapper classManagementMapper;

    @Override
    @Transactional
    public Result insert(StudentInfo studentInfo) {
        // 1. 校验学号是否已存在
        QueryWrapper<StudentInfo> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", studentInfo.getStudentNo());
        if (studentManagementMapper.selectCount(studentWrapper) > 0) {
            return new Result(0, "学号已存在", null);
        }

        // 2. 校验登录账号是否已存在
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", studentInfo.getStudentNo());
        if (userMapper.selectCount(userWrapper) > 0) {
            return new Result(0, "登录账号已存在", null);
        }

        // 3. 插入学生信息
        studentInfo.setId(IdWorker.getId());
        studentManagementMapper.insert(studentInfo);

        // 4. 插入用户登录信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(IdWorker.getId());
        userInfo.setUserId(studentInfo.getStudentNo());  // 用学号作为登录账号
        userInfo.setUsername(studentInfo.getName());
        userInfo.setPassword("123456");
        userInfo.setStatus(2);  // 学生
        userInfo.setInfoId(studentInfo.getId());
        userInfo.setIsDele(0);
        userMapper.insert(userInfo);

        // 5. 插入班级关联
        if (studentInfo.getClassId() != null) {
            ClassStudent cs = new ClassStudent();
            cs.setId(IdWorker.getId());
            cs.setClassId(studentInfo.getClassId());
            cs.setStudentId(studentInfo.getId());
            cs.setIsDele(0);
            classStudentMapper.insert(cs);
        }

        return new Result(1, "success", userInfo.getUserId());
    }

    @Override
    public Result list(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<StudentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);

        if (params.get("studentNo") != null && !params.get("studentNo").toString().isEmpty()) {
            wrapper.like("student_no", params.get("studentNo").toString());
        }
        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            wrapper.like("name", params.get("name").toString());
        }
        if (params.get("grade") != null && !params.get("grade").toString().isEmpty()) {
            wrapper.eq("grade", params.get("grade").toString());
        }
        if (params.get("majorName") != null && !params.get("majorName").toString().isEmpty()) {
            QueryWrapper<Major> majorWrapper = new QueryWrapper<>();
            majorWrapper.like("major_name", params.get("majorName").toString());
            majorWrapper.eq("is_dele", 0);
            List<Major> majorList = majorManagementMapper.selectList(majorWrapper);
            List<Long> majorIds = majorList.stream().map(Major::getId).collect(Collectors.toList());
            if (!majorIds.isEmpty()) {
                wrapper.in("major_id", majorIds);
            } else {
                wrapper.eq("major_id", -1L);
            }
        }
        if (params.get("className") != null && !params.get("className").toString().isEmpty()) {
            QueryWrapper<ClassInfo> classWrapper = new QueryWrapper<>();
            classWrapper.like("class_name", params.get("className").toString());
            classWrapper.eq("is_dele", 0);
            List<ClassInfo> classList = classManagementMapper.selectList(classWrapper);
            List<Long> classIds = classList.stream().map(ClassInfo::getId).collect(Collectors.toList());
            if (!classIds.isEmpty()) {
                QueryWrapper<ClassStudent> csWrapper = new QueryWrapper<>();
                csWrapper.in("class_id", classIds);
                csWrapper.eq("is_dele", 0);
                List<ClassStudent> csList = classStudentMapper.selectList(csWrapper);
                List<Long> studentIds = csList.stream().map(ClassStudent::getStudentId).collect(Collectors.toList());
                if (!studentIds.isEmpty()) {
                    wrapper.in("id", studentIds);
                } else {
                    wrapper.eq("id", -1L);
                }
            } else {
                wrapper.eq("id", -1L);
            }
        }

        wrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNum, pageSize);
        List<StudentInfo> list = studentManagementMapper.selectList(wrapper);

        // 填充班级ID
        for (StudentInfo student : list) {
            QueryWrapper<ClassStudent> csWrapper = new QueryWrapper<>();
            csWrapper.eq("student_id", student.getId());
            csWrapper.eq("is_dele", 0);
            ClassStudent cs = classStudentMapper.selectOne(csWrapper);
            if (cs != null) {
                student.setClassId(cs.getClassId());
            }
        }

        PageInfo<StudentInfo> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return new Result(1, "success", result);
    }

    @Override
    @Transactional
    public Result update(StudentInfo studentInfo) {
        // 1. 校验学生是否存在
        StudentInfo exist = studentManagementMapper.selectById(studentInfo.getId());
        if (exist == null) {
            return new Result(0, "学生不存在", null);
        }

        // 2. 更新学生信息（学号不允许修改）
        studentManagementMapper.updateById(studentInfo);

        // 3. 同步更新用户登录信息中的姓名
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", exist.getStudentNo());
        UserInfo userInfo = userMapper.selectOne(userWrapper);
        if (userInfo != null) {
            userInfo.setUsername(studentInfo.getName());
            userMapper.updateById(userInfo);
        }

        // 4. 处理班级关联
        if (studentInfo.getClassId() != null) {
            QueryWrapper<ClassStudent> csWrapper = new QueryWrapper<>();
            csWrapper.eq("student_id", studentInfo.getId());
            csWrapper.eq("is_dele", 0);
            ClassStudent existCs = classStudentMapper.selectOne(csWrapper);
            if (existCs != null) {
                if (!existCs.getClassId().equals(studentInfo.getClassId())) {
                    existCs.setClassId(studentInfo.getClassId());
                    classStudentMapper.updateById(existCs);
                }
            } else {
                ClassStudent cs = new ClassStudent();
                cs.setId(IdWorker.getId());
                cs.setClassId(studentInfo.getClassId());
                cs.setStudentId(studentInfo.getId());
                cs.setIsDele(0);
                classStudentMapper.insert(cs);
            }
        }

        return new Result(1, "success", null);
    }

    @Override
    @Transactional
    public Result delete(String userId) {
        // 1. 逻辑删除用户登录信息
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.eq("user_id", userId);
        UserInfo userInfo = userMapper.selectOne(userWrapper);
        if (userInfo == null) {
            return new Result(0, "用户不存在", null);
        }
        userInfo.setIsDele(1);
        userMapper.updateById(userInfo);

        // 2. 逻辑删除学生信息
        QueryWrapper<StudentInfo> studentWrapper = new QueryWrapper<>();
        studentWrapper.eq("student_no", userId);
        StudentInfo studentInfo = studentManagementMapper.selectOne(studentWrapper);
        if (studentInfo == null) {
            return new Result(0, "学生不存在", null);
        }
        studentInfo.setIsDele(1);
        studentManagementMapper.updateById(studentInfo);

        // 3. 逻辑删除班级关联
        QueryWrapper<ClassStudent> csWrapper = new QueryWrapper<>();
        csWrapper.eq("student_id", studentInfo.getId());
        csWrapper.eq("is_dele", 0);
        ClassStudent cs = classStudentMapper.selectOne(csWrapper);
        if (cs != null) {
            cs.setIsDele(1);
            classStudentMapper.updateById(cs);
        }

        return new Result(1, "success", null);
    }

    @Override
    public Result resetPassword(String userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        UserInfo userInfo = userMapper.selectOne(wrapper);
        if (userInfo == null) {
            return new Result(0, "用户不存在", null);
        }
        userInfo.setPassword("123456");
        userMapper.updateById(userInfo);
        return new Result(1, "success", null);
    }
}
