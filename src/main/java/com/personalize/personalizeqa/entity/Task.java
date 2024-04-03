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
    @TableField("start_time")
    private Long startTime;
    @TableField("end_time")
    private Long endTime;
    @TableField("task_info_source_name")
    private String taskSourceName;
    @TableField("task_rule")
    private String taskRule;
    @TableField("task_note")
    private String taskNote;
    @TableField("status")
    private boolean status;
    @Builder
    public Task(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime, String updateUser,
                String taskName,String taskCollectionName, String charge, Long startTime,Long endTime,
                String taskSourceName,String taskRule,String taskNote,Boolean status){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.taskName = taskName;
        this.taskCollectionName = taskCollectionName;
        this.charge = charge;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskSourceName = taskSourceName;
        this.taskRule = taskRule;
        this.taskNote = taskNote;
        this.status = status;
    }
}
