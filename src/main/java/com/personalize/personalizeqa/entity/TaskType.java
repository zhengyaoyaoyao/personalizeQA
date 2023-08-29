package com.personalize.personalizeqa.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@TableName("qa_task_type")
public class TaskType {
    @TableId(value = "id",type = IdType.INPUT)
    @NotNull(message = "id不能为空",groups = SuperEntity.Update.class)
    private String id;
    @TableField("type_name")
    private String typeName;
    @TableField("type_desc")
    private String typeDesc;
}
