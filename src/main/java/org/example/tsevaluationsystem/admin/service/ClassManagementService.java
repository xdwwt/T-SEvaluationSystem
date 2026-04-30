package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface ClassManagementService {
    Result insert(ClassInfo classInfo);
    Result list(Map<String, Object> params);
    Result update(ClassInfo classInfo);
    Result delete(Long id);

    Result listAll();

    // 班级学生管理
    Result listClassStudents(Long classId);
    Result listUnassignedStudents();
    Result addStudentToClass(Long classId, Long studentId);
    Result removeStudentFromClass(Long id);
}
