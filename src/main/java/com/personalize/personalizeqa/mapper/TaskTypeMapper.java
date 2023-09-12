package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.TaskType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskTypeMapper extends BaseMapper<TaskType> {
    @Select("select id,type_name from qa_task_type")
    List<Map<String,String>> selectTaskTypeNames();
}
