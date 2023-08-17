package com.personalize.personalizeqa.dto;

import com.personalize.personalizeqa.entity.DataSet;
import lombok.Data;

import java.util.List;

@Data
public class DataSetListDTO {
    private List<DataSet> datas;
    private Integer count;
}
