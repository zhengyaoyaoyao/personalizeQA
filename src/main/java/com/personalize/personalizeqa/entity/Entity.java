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
@TableName("qa_entity")
public class Entity extends En<String> {
    @TableField("name")
    private String entityName;
    @TableField("annotation_name")
    private String annotationName;
    @TableField("entity_desc")
    private String description;
    @Builder
    public Entity(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime,String updateUser,
                  String entityName,String annotationName,String description){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.entityName = entityName;
        this.annotationName = annotationName;
        this.description = description;
    }
}
