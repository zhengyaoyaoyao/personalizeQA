package com.personalize.personalizeqa.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TasksListVO {
    private String id;
    private String taskName;
    private String dataSetName;
    private String taskType;
    private String dataType;
    private Long fileNumber;
    private String taskUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
