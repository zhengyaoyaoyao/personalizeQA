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
@TableName("qa_annotation_task")
public class AnnotationTask extends En<String> {
    private static final long serialVersionUID = 1L;
    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;
    /**
     * 任务描述
     */
    @TableField("task_desc")
    private String taskDesc;

    /**
     * 外键，需要带上数据集的id
     */
    @TableField("task_dataset_id")
    private String taskDataSetId;
    /**
     * 外键，索引具体的任务类型的id
     */
    @TableField("task_type_id")
    private String taskTypeId;

    /**
     * 有多少个文件
     */
    @TableField("file_total")
    private Long fileTotal;
    /**
     * 数据的类型，即标记的是doc还是img等
     */
    @TableField("data_type")
    private String dataType;
    /**
     * 是否完成标注
     */
    @TableField("is_finish")
    private boolean isFinish;
    /**
     * 任务规则
     */
    @TableField("task_rule")
    private String taskRule;
    /**
     * 数据集创建的年月
     */
    @TableField("task_user")
    private String taskUser;
    /**
     * 创建日期
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
    public AnnotationTask(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime,
                          String updateUser, String taskName, String taskDesc, String taskDataSetId, String taskTypeId, Long fileTotal, String dataType, Boolean isFinish,
                          String taskRule, String taskUser, String createMonth, String createWeek, String createDay){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskDataSetId = taskDataSetId;
        this.taskTypeId = taskTypeId;
        this.fileTotal = fileTotal;
        this.dataType = dataType;
        this.taskRule = taskRule;
        this.isFinish = isFinish;
        this.taskUser = taskUser;
        this.createMonth = createMonth;
        this.createWeek = createWeek;
        this.createDay = createDay;
    }
}
