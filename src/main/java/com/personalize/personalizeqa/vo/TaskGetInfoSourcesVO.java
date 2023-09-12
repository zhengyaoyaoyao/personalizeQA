package com.personalize.personalizeqa.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskGetInfoSourcesVO {
    @TableId("id")
    private String id;
    @TableField("info_source_name")
    private String infoSourceName;
    @TableField("info_source_rule")
    private String infoSourceRule;
}
