package com.personalize.personalizeqa.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskNewFilesListVO {
    private List<Map<String,String>> fileNames;
}
