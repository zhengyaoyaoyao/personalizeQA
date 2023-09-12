package com.personalize.personalizeqa.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskTypeListVO {
    private List<Map<String,String>> taskTypeList;
}
