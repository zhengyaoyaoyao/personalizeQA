package com.personalize.personalizeqa.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TaskInfoVO {
    private String id;
    private String taskName;
    private String taskDesc;
    private String taskDataset;
    private String taskType;
    private List<String> entities;
    private List<String> relations;
    private List<String> files;
    private Long fileTotal;
    private String dataType;
    private String taskRule;
    private Boolean isFinish;
    private String taskUser;
    private String createMonth;
    private String createWeek;
    private String createDay;
    private LocalDateTime createTime;
    private String createUser;
    private LocalDateTime updateTime;
    private String updateUser;
}
