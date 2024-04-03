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
@TableName("qa_info_source")
public class InfoSource extends En<String>{
    @TableField("info_source_name")
    private String infoSourceName;
    @TableField("info_source_url")
    private String infoSourceUrl;
    @TableField("info_source_api")
    private String infoSourceApi;
    @TableField("info_source_rule")
    private String infoSourceRule;
    @TableField("info_source_desc")
    private String infoSourceDesc;
    @Builder
    public InfoSource(String id, LocalDateTime createTime, String createUser, LocalDateTime updateTime, String updateUser,
                      String infoSourceName,String infoSourceApi,String infoSourceUrl,String infoSourceRule,String infoSourceDesc){
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.infoSourceName = infoSourceName;
        this.infoSourceUrl = infoSourceUrl;
        this.infoSourceApi = infoSourceApi;
        this.infoSourceRule = infoSourceRule;
        this.infoSourceDesc = infoSourceDesc;
    }
}
