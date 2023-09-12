package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.Relation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RelationMapper extends BaseMapper<Relation> {
    @Select("select id,rel_name from qa_relation")
    List<Map<String,String>> selectRelationNames();
}
