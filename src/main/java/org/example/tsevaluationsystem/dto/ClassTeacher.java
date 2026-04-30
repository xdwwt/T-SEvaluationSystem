package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_class_teacher")
public class ClassTeacher {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long classId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long teacherId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long courseId;
    private String semester;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private String className;

    @TableField(exist = false)
    private String teacherName;

    @TableField(exist = false)
    private String courseName;
}
