package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("tb_score")
public class Score {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long studentId;
    private Long courseId;
    private Long teacherId;
    private Long classId;
    private String semester;
    private BigDecimal score;
    private String comment;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
