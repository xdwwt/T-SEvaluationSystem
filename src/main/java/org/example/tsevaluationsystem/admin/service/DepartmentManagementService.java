package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Department;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface DepartmentManagementService {
    Result insert(Department department);
    Result list(Map<String, Object> params);
    Result listAll();
    Result update(Department department);
    Result delete(Long id);
}
