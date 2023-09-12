package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.Task;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    //保存信息到任务-人员
    @Insert("insert into qa_task_user (id,task_id,username) values(#{id},#{taskId},#{username})")
    public Boolean saveTaskUser(@Param("id")String id,@Param("taskId")String taskId,@Param("username")String username);
    @Select(("select info_source_name from qa_info_source where id=#{id}"))
    public String getInfoSourceNameById(@Param("id") String id);
    @Select(("select username from qa_task_user where task_id=#{taskId}"))
    public List<String> getMembersByTaskId(@Param("taskId")String id);

    @Delete("delete from qa_task_user where task_id=#{taskId} ")
    Boolean deleteMembersByTaskId(@Param("taskId")String id);

    //通过任务id查出所有包含的文件列表id
    @Select("select id from qa_infosource_files where task_id=#{taskId}")
    List<String> getInfoSourceFilesByTaskId(String taskId);
}
