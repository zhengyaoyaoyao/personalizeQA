package com.personalize.personalizeqa.dto;

import com.personalize.personalizeqa.entity.DataSet;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DataSetListDTO {
    private List<DataSet> datas;
    private Integer count;
}
