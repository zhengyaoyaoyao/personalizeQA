package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select authority_code from qa_authority where authority_name = #{authority}")
    public List<String> getAuthorities(String authority);
    @Select("select organization,group_concat(username) as usernames from qa_user group by organization")
    List<Map<String,List<String>>> selectUsersByOrganization();
}
