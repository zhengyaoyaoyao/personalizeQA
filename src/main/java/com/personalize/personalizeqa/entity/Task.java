package com.personalize.personalizeqa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName("qa_task")
public class Task  extends En<String>{
    @TableField("task_name")
    private String taskName;
    @TableField("task_collection_name")
    private String taskCollectionName;
    @TableField("charge")
    private String charge;
    @TableField("task_time")
    private String taskTime;
    @TableField("task_info_source_name")
    private String taskSourceName;
    @TableField("task_rule")
    private String taskRule;
    @TableField("status")
    private boolean status;
    @Builder
    public Task(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime, String updateUser,
                String taskName,String taskCollectionName, String charge, String taskTime,
                String taskSourceName,String taskRule,Boolean status){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.taskName = taskName;
        this.taskCollectionName = taskCollectionName;
        this.charge = charge;
        this.taskTime = taskTime;
        this.taskSourceName = taskSourceName;
        this.taskRule = taskRule;
        this.status = status;
    }
}
