package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Major;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface MajorManagementService {
    Result insert(Major major);
    Result list(Map<String, Object> params);
    Result update(Major major);
    Result delete(Long id);
    Result listAll(Map<String, Object> params);
}
