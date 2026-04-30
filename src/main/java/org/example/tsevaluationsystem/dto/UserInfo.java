package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
@TableName("tb_user_info")
public class UserInfo {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String userId;
    private String username;
    private String password;

    /**
     * 身份
     * 0：管理员
     * 1：教师
     * 2：学生
     */
    private Integer status;

    /**
     * 关联教师/学生表ID
     * 管理员为null
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long infoId;

    /**
     * 删除标志
     * 0：未删除
     * 1：已删除
     */
    private Integer isDele;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
}
