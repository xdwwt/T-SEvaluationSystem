package org.example.tsevaluationsystem.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.tsevaluationsystem.admin.mapper.ClassManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.ClassStudentMapper;
import org.example.tsevaluationsystem.admin.mapper.DepartmentManagementMapper;
import org.example.tsevaluationsystem.admin.mapper.MajorManagementMapper;
import org.example.tsevaluationsystem.admin.service.ClassManagementService;
import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.ClassStudent;
import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 班级管理服务实现类
 */
@Service
public class ClassManagementServiceImpl implements ClassManagementService {

    @Autowired
    private ClassManagementMapper classManagementMapper;
    @Autowired
    private ClassStudentMapper classStudentMapper;
    @Autowired
    private MajorManagementMapper majorManagementMapper;
    @Autowired
    private DepartmentManagementMapper departmentManagementMapper;

    /**
     * 新增班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    @Override
    public Result insert(ClassInfo classInfo) {
        classInfo.setId(IdWorker.getId());
        classManagementMapper.insert(classInfo);
        return new Result(1, "success", null);
    }

    /**
     * 查询班级列表（分页）
     * @param params 查询参数
     * @return 班级列表
     */
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
        if (params.get("deptName") != null && !params.get("deptName").toString().isEmpty()) {
            QueryWrapper<Department> deptWrapper = new QueryWrapper<>();
            deptWrapper.like("dept_name", params.get("deptName").toString());
            deptWrapper.eq("is_dele", 0);
            List<Department> deptList = departmentManagementMapper.selectList(deptWrapper);
            List<Long> deptIds = deptList.stream().map(Department::getId).collect(Collectors.toList());
            if (!deptIds.isEmpty()) {
                QueryWrapper<Major> majorWrapper = new QueryWrapper<>();
                majorWrapper.in("department_id", deptIds);
                majorWrapper.eq("is_dele", 0);
                List<Major> majorList = majorManagementMapper.selectList(majorWrapper);
                List<Long> majorIds = majorList.stream().map(Major::getId).collect(Collectors.toList());
                if (!majorIds.isEmpty()) {
                    wrapper.in("major_id", majorIds);
                } else {
                    wrapper.eq("major_id", -1L);
                }
            } else {
                wrapper.eq("major_id", -1L);
            }
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

    /**
     * 编辑班级
     * @param classInfo 班级信息
     * @return 操作结果
     */
    @Override
    public Result update(ClassInfo classInfo) {
        classManagementMapper.updateById(classInfo);
        return new Result(1, "success", null);
    }

    /**
     * 查询所有班级
     * @return 班级列表
     */
    @Override
    public Result listAll() {
        QueryWrapper<ClassInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_dele", 0);
        wrapper.orderByAsc("class_name");
        List<ClassInfo> list = classManagementMapper.selectList(wrapper);
        return new Result(1, "success", list);
    }

    /**
     * 删除班级（逻辑删除）
     * @param id 班级ID
     * @return 操作结果
     */
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

    /**
     * 查询班级学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    @Override
    public Result listClassStudents(Long classId) {
        List<Map<String, Object>> list = classStudentMapper.selectStudentsByClassId(classId);
        return new Result(1, "success", list);
    }

    /**
     * 查询未分配班级的学生
     * @return 未分配学生列表
     */
    @Override
    public Result listUnassignedStudents() {
        List<Map<String, Object>> list = classStudentMapper.selectUnassignedStudents();
        return new Result(1, "success", list);
    }

    /**
     * 将学生加入班级
     * @param classId 班级ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result addStudentToClass(Long classId, Long studentId) {
        QueryWrapper<ClassStudent> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", classId);
        wrapper.eq("student_id", studentId);
        ClassStudent exist = classStudentMapper.selectOne(wrapper);
        if (exist != null) {
            if (exist.getIsDele() == 0) {
                return new Result(0, "该学生已在此班级中", null);
            }
            exist.setIsDele(0);
            classStudentMapper.updateById(exist);
            return new Result(1, "success", null);
        }
        ClassStudent cs = new ClassStudent();
        cs.setId(IdWorker.getId());
        cs.setClassId(classId);
        cs.setStudentId(studentId);
        cs.setIsDele(0);
        classStudentMapper.insert(cs);
        return new Result(1, "success", null);
    }

    /**
     * 将学生移出班级
     * @param id 班级学生关联ID
     * @return 操作结果
     */
    @Override
    public Result removeStudentFromClass(Long id) {
        ClassStudent cs = classStudentMapper.selectById(id);
        if (cs == null) {
            return new Result(0, "记录不存在", null);
        }
        cs.setIsDele(1);
        classStudentMapper.updateById(cs);
        return new Result(1, "success", null);
    }
}
