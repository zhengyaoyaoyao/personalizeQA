package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper extends BaseMapper<File> {
    @Select("select id,submitted_file_name from qa_file where folder=#{folder}")
    List<Map<String,String>> selectFileNameByFolder(@Param("folder")String folder);
}
