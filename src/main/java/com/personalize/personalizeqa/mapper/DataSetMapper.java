package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.DataSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataSetMapper extends BaseMapper<DataSet> {
    @Select("select id,data_name from qa_dataset")
    List<Map<String,String>> selectDatasetNames();
}
