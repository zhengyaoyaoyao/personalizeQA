package com.personalize.personalizeqa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personalize.personalizeqa.entity.AnnotationTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnotationTaskMapper extends BaseMapper<AnnotationTask> {
    //填写中间表qa_task_entity
    @Insert("insert into qa_task_entity (id,task_id,entity_id) values (#{id},#{taskId},#{entityId})")
    boolean insertEntityList(@Param("id")String id,@Param("taskId")String taskId,@Param("entityId")String entityId);
    @Insert("insert into qa_task_file (id,task_id,file_id) values (#{id},#{taskId},#{fileId})")
    boolean insertFileList(@Param("id")String id,@Param("taskId")String taskId,@Param("fileId")String fileId);
    @Insert("insert into qa_task_relation (id,task_id,relation_id) values (#{id},#{taskId},#{relationId})")
    boolean insertRelationList(@Param("id")String id,@Param("taskId")String taskId,@Param("relationId")String relationId);
    /**
     * 根据task_dataset_id从qa_dataset中查询数据集名称
     * 根据task_type_id 从qa_task_type中查询类型名称
     */
    @Select("select data_name from qa_dataset where id = #{taskDataSetId}")
    String selectDatasetNameByTaskDataSetId(@Param("taskDataSetId")String taskDataSetId);
    @Select("select type_name from qa_task_type where id = #{taskTypeId}")
    String selectTypeNameByTaskDataSetId(@Param("taskTypeId")String taskTypeId);

    //删除中间表的信息,qa_task_entity,qa_task_relation,qa_task_file
    @Delete("delete from qa_task_entity where task_id=#{taskId} ")
    int deleteTaskEntityByTaskId(@Param("taskId")String taskId);
    @Delete("delete from qa_task_relation where task_id=#{taskId}")
    int deleteTaskRelationByTaskId(@Param("taskId")String taskId);
    @Delete("delete from qa_task_file where task_id=#{taskId}")
    int deleteTaskFilesByTaskId(@Param("taskId")String taskId);
    //删除前需要先查询是否已经是0条，如果是0条，那么删除int为0也合理
    @Select("select count(id) from qa_task_entity where task_id=#{taskId} ")
    int selectTaskEntityCountByTaskId(@Param("taskId")String taskId);
    @Select("select count(id) from qa_task_relation where task_id=#{taskId}")
    int selectTaskRelationCountByTaskId(@Param("taskId")String taskId);
    @Select("select count(id)e from qa_task_file where task_id=#{taskId}")
    int selectTaskFilesCountByTaskId(@Param("taskId")String taskId);

    //三张中间表进行关联查询，去qa_entity、qa_realtion、qa_file中查询他们对应的名称list
    @Select("select e.name from qa_entity e join qa_task_entity te on e.id = te.entity_id where te.task_id =#{taskId}")
    List<String> selectTaskEntityNamesByTaskId(@Param("taskId")String taskId);
    @Select("select e.rel_name from qa_relation e join qa_task_relation te on e.id = te.relation_id where te.task_id = #{taskId}")
    List<String> selectTaskRelationNamesByTaskId(@Param("taskId")String taskId);
    @Select("select e.submitted_file_name from qa_file e join qa_task_file te on e.id =te.file_id where te.task_id =#{taskId}")
    List<String> selectTaskFileNamesByTaskId(@Param("taskId")String taskId);

}
