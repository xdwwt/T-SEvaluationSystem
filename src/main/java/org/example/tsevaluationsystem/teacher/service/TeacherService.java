package org.example.tsevaluationsystem.teacher.service;

import org.example.tsevaluationsystem.dto.Result;

import java.util.List;
import java.util.Map;

public interface TeacherService {

    Result getEvaluationList(Long teacherId);

    Result getTeachingClasses(Long teacherId);

    Result getClassStudentsWithScore(Long teacherId, Long classId, Long courseId, String semester);

    Result submitScores(Long teacherId, List<Map<String, Object>> scoreList);

    Result getScoreList(Long teacherId);
}
