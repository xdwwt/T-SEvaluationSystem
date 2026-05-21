package org.example.tsevaluationsystem.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.tsevaluationsystem.dto.ClassStudent;

import java.util.List;
import java.util.Map;

/**
 * 班级学生关联Mapper接口
 */
@Mapper
public interface ClassStudentMapper extends BaseMapper<ClassStudent> {

    /**
     * 根据班级ID查询学生列表
     * @param classId 班级ID
     * @return 学生列表
     */
    @Select("SELECT cs.id, s.id as studentId, s.student_no as studentNo, s.name, s.gender, s.grade, m.major_name as major " +
            "FROM tb_class_student cs " +
            "JOIN tb_student_info s ON cs.student_id = s.id " +
            "LEFT JOIN tb_major m ON s.major_id = m.id " +
            "WHERE cs.class_id = #{classId} AND cs.is_dele = 0 AND s.is_dele = 0 " +
            "ORDER BY s.student_no")
    List<Map<String, Object>> selectStudentsByClassId(@Param("classId") Long classId);

    /**
     * 查询未分配班级的学生列表
     * @return 未分配学生列表
     */
    @Select("SELECT s.id, s.student_no as studentNo, s.name, s.gender, s.grade, m.major_name as major " +
            "FROM tb_student_info s " +
            "LEFT JOIN tb_major m ON s.major_id = m.id " +
            "WHERE s.is_dele = 0 AND s.id NOT IN (" +
            "  SELECT student_id FROM tb_class_student WHERE is_dele = 0" +
            ") " +
            "ORDER BY s.student_no")
    List<Map<String, Object>> selectUnassignedStudents();
}
