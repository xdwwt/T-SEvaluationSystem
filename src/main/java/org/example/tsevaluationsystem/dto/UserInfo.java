package org.example.tsevaluationsystem.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tb_user_info")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private int id;
    private String username;
    private String password;
    /**
     * 用户类别
     * 0：管理员
     * 1：教师
     * 2：学生
     */
    private int status;
}
