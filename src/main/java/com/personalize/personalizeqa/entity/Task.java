package com.personalize.personalizeqa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
@TableName("qa_task")
public class Task  extends En<String> {
    private static final long serialVersionUID = 1L;
    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;
    /**
     * 任务描述
     */
    @TableField("type_desc")
    private String taskDesc;
    @TableField("task_dataset")
    /**
     * 外键，需要带上数据集的id
     */
    private String taskDataSet;
    /**
     * 外键，索引具体的任务类型的id
     */
    @TableField("task_type")
    private String taskType;
    /**
     * 数据的类型，即标记的是doc还是img等
     */
    @TableField("data_type")
    private String dataType;
    /**
     * 任务规则
     */
    @TableField("task_rule")
    private String taskRule;
    /**
     * 数据集创建的年月
     */
    @TableField(value = "create_month")
    private String createMonth;
    /**
     * 数据集创建所属的周
     */
    @TableField(value = "create_week")
    private String createWeek;
    /**
     * 数据集创建年月日
     */
    @TableField(value = "create_day")
    private String createDay;
    @Builder
    public Task(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime,
                String updateUser,String taskName,String taskDesc,String taskDataSet,String taskType,String dataType,
                String taskRule,String createMonth,String createWeek,String createDay){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDataSet = taskDataSet;
        this.taskType = taskType;
        this.dataType = dataType;
        this.taskRule = taskRule;
        this.createMonth = createMonth;
        this.createWeek = createWeek;
        this.createDay = createDay;
    }
}
