package com.personalize.personalizeqa.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
@TableName("qa_log")
public class Log {
    @TableId(value = "id",type = IdType.INPUT)
    private String id;
    @TableField("action_name")
    private String actionName;
    @TableField("action_user")
    private String actionUser;
    @TableField("action_time")
    private LocalDateTime actionTime;
    @TableField("action_authority")
    private String actionAuthority;
}
