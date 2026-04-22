package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_class_teacher")
public class ClassTeacher {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long classId;
    private Long teacherId;
    private Long courseId;
    private String semester;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
}
