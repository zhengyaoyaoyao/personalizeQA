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
@TableName("qa_relation")
public class Relation extends En<String> {
    @TableField("rel_name")
    private String relationName;
    @TableField("annotation_name")
    private String annotationName;
    @TableField("rel_desc")
    private String description;
    @Builder
    public Relation(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime,String updateUser,
                  String relationName,String annotationName,String description){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.relationName = relationName;
        this.annotationName = annotationName;
        this.description = description;
    }
}
