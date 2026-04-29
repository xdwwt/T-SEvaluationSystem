package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.ClassInfo;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface ClassManagementService {
    Result insert(ClassInfo classInfo);
    Result list(Map<String, Object> params);
    Result update(ClassInfo classInfo);
    Result delete(Long id);
}
