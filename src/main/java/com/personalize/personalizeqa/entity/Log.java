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
    @TableField("description")
    private String description;
    @TableField("action_user")
    private String actionUser;
    @TableField("action_time")
    private LocalDateTime actionTime;
    @TableField("action_authority")
    private String actionAuthority;
    @TableField("ip")
    private String ip;
    @TableField("method")
    private String method;
    @TableField("params")
    private String params;
    @TableField("operation_type")
    private Integer operationType;
    @TableField("result")
    private String result;
    @TableField("duration")
    private Long duration;
}
