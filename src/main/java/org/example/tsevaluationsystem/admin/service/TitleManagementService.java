package org.example.tsevaluationsystem.admin.service;

import org.example.tsevaluationsystem.dto.Result;
import org.example.tsevaluationsystem.dto.Title;

import java.util.Map;

public interface TitleManagementService {
    Result insert(Title title);
    Result list(Map<String, Object> params);
    Result listAll();
    Result update(Title title);
    Result delete(Long id);
}
