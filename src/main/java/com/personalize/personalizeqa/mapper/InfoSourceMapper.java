package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.vo.TaskGetInfoSourcesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface InfoSourceMapper extends BaseMapper<InfoSource> {
    @Select("select id,info_source_name from qa_info_source")
    List<Map<String,String>> selectInfoSourceNames();

    //查询放在新建任务上的信源信息
    @Select("select id,info_source_name,info_source_rule from qa_info_source")
    List<TaskGetInfoSourcesVO> getInfoSources();
}
