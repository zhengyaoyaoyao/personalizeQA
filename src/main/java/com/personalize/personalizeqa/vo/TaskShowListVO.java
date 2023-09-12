package com.personalize.personalizeqa.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskShowListVO {
    private String id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private String taskName;
    private String charge;
    private List<String> members;
    private String taskTime;
    private String taskCollectionName;
    private String infoSourceName;
    private String infoSourceRule;
    private boolean status;
}
