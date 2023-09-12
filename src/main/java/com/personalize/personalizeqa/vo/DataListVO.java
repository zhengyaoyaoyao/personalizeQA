package com.personalize.personalizeqa.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataListVO {
    /**
     * 数据集列表
     */
    private List<Map<String,String>> dataSetList;
}
