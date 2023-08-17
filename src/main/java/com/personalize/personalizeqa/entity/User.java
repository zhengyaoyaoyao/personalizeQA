package com.personalize.personalizeqa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("qa_user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String username;
    private String phone;
    private String nick_name;
    private String authority;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    @TableField("login_time")
    private LocalDateTime loginTime;
}
