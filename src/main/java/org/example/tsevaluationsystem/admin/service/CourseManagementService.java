package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Course;
import org.example.tsevaluationsystem.dto.Result;

import java.util.Map;

public interface CourseManagementService {
    Result insert(Course course);
    Result list(Map<String, Object> params);
    Result update(Course course);
    Result delete(Long id);

    Result listAll();
}
