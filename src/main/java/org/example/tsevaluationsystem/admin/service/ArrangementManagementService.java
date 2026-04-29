package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.ClassTeacher;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface ArrangementManagementService {
    Result insert(ClassTeacher classTeacher);
    Result list(Map<String, Object> params);
    Result update(ClassTeacher classTeacher);
    Result delete(Long id);
}
