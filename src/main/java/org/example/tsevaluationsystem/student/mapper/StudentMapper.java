package org.example.tsevaluationsystem.student.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.tsevaluationsystem.dto.Score;
import org.example.tsevaluationsystem.dto.TeacherEvaluation;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper extends BaseMapper<TeacherEvaluation> {

    /**
     * 查询学生所在班级的授课教师列表（排除已评价的，且成绩已发放）
     */
    @Select("SELECT " +
            "  CAST(ct.id AS CHAR) as id, CAST(ct.teacher_id AS CHAR) as teacherId, CAST(ct.course_id AS CHAR) as courseId, ct.semester, " +
            "  t.name as teacherName, c.course_name as courseName " +
            "FROM tb_class_student cs " +
            "JOIN tb_class_teacher ct ON cs.class_id = ct.class_id " +
            "JOIN tb_teacher_info t ON ct.teacher_id = t.id " +
            "JOIN tb_course c ON ct.course_id = c.id " +
            "JOIN tb_score sc ON sc.student_id = cs.student_id " +
            "  AND sc.teacher_id = ct.teacher_id " +
            "  AND sc.course_id = ct.course_id " +
            "  AND sc.semester = ct.semester " +
            "  AND sc.is_dele = 0 " +
            "WHERE cs.student_id = #{studentId} " +
            "  AND cs.is_dele = 0 " +
            "  AND ct.is_dele = 0 " +
            "  AND t.is_dele = 0 " +
            "  AND c.is_dele = 0 " +
            "  AND NOT EXISTS (" +
            "    SELECT 1 FROM tb_teacher_evaluation te " +
            "    WHERE te.student_id = #{studentId} " +
            "      AND te.teacher_id = ct.teacher_id " +
            "      AND te.course_id = ct.course_id " +
            "      AND te.semester = ct.semester " +
            "      AND te.is_dele = 0" +
            "  )")
    List<Map<String, Object>> selectTeachersToEvaluate(@Param("studentId") Long studentId);

    /**
     * 学生评价后，将对应成绩设为可查看
     */
    @Update("UPDATE tb_score SET is_viewable = 1 " +
            "WHERE student_id = #{studentId} " +
            "  AND teacher_id = #{teacherId} " +
            "  AND course_id = #{courseId} " +
            "  AND semester = #{semester} " +
            "  AND is_dele = 0")
    int updateScoreViewable(@Param("studentId") Long studentId,
                            @Param("teacherId") Long teacherId,
                            @Param("courseId") Long courseId,
                            @Param("semester") String semester);

    /**
     * 查询学生已提交的评价记录
     */
    @Select("SELECT " +
            "  CAST(te.id AS CHAR) as id, CAST(te.teacher_id AS CHAR) as teacherId, CAST(te.student_id AS CHAR) as studentId, CAST(te.course_id AS CHAR) as courseId, " +
            "  te.semester, te.teaching_score as teachingScore, te.content_score as contentScore, " +
            "  te.method_score as methodScore, te.effect_score as effectScore, te.total_score as totalScore, " +
            "  te.create_time as createTime, " +
            "  t.name as teacherName, c.course_name as courseName " +
            "FROM tb_teacher_evaluation te " +
            "JOIN tb_teacher_info t ON te.teacher_id = t.id " +
            "JOIN tb_course c ON te.course_id = c.id " +
            "WHERE te.student_id = #{studentId} " +
            "  AND te.is_dele = 0 " +
            "ORDER BY te.create_time DESC")
    List<Map<String, Object>> selectEvaluationList(@Param("studentId") Long studentId);

    /**
     * 查询学生成绩列表
     */
    @Select("SELECT " +
            "  CAST(s.id AS CHAR) as id, CAST(s.student_id AS CHAR) as studentId, CAST(s.course_id AS CHAR) as courseId, CAST(s.teacher_id AS CHAR) as teacherId, " +
            "  CAST(s.class_id AS CHAR) as classId, s.semester, s.usual_score as usualScore, s.final_score as finalScore, " +
            "  s.score, s.comment, s.is_viewable as isViewable, s.create_time as createTime, " +
            "  c.course_name as courseName, t.name as teacherName " +
            "FROM tb_score s " +
            "JOIN tb_course c ON s.course_id = c.id " +
            "JOIN tb_teacher_info t ON s.teacher_id = t.id " +
            "WHERE s.student_id = #{studentId} " +
            "  AND s.is_dele = 0 " +
            "  AND s.is_viewable = 1 " +
            "ORDER BY s.create_time DESC")
    List<Map<String, Object>> selectScoreList(@Param("studentId") Long studentId);

    /**
     * 查询成绩记录数（用于校验成绩是否已发放）
     */
    @Select("SELECT COUNT(*) FROM tb_score ${ew.customSqlSegment}")
    long selectScoreCount(@Param(Constants.WRAPPER) QueryWrapper<Score> wrapper);
}
