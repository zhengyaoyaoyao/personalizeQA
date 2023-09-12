package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.AnnotationTask;
import com.personalize.personalizeqa.exception.BizException;
import com.personalize.personalizeqa.mapper.AnnotationTaskMapper;
import com.personalize.personalizeqa.server.IAnnotationTaskService;
import com.personalize.personalizeqa.utils.DateUtils;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.TaskInfoVO;
import com.personalize.personalizeqa.vo.TasksListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class AnnotationTaskServiceImpl extends ServiceImpl<AnnotationTaskMapper, AnnotationTask> implements IAnnotationTaskService {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private AnnotationTaskMapper annotationTaskMapper;

    /**
     * 新建任务
     * @param taskName
     * @param taskDatasetId
     * @param annotationFilesId
     * @param taskTypeId
     * @param dataType
     * @param entityList
     * @param relationList
     * @param taskRule
     * @param taskDesc
     * @param taskUser
     * @return
     */
    @Override
    public boolean insert(String taskName, String taskDatasetId, List<String> annotationFilesId, String taskTypeId, String dataType, List<String> entityList, List<String> relationList, String taskRule, String taskDesc, String taskUser) {
        //表的保存涉及：qa_task,qa_task_entity,qa_task_file,qa_task_relation,qa_task_user.
        //qa_task_file、qa_task_entity和qa_task_relation是n:m的表，所以不在这里保存entityList和relationList信息，需要传递到具体的Mapper去保存
        //user因为还没有设置，所以暂时直接填。
        //1. 设置表qa_task
        LocalDateTime now = LocalDateTime.now();
        String taskId = idGenerate.generate().toString();
        AnnotationTask task = AnnotationTask.builder().id(taskId).taskName(taskName).taskDataSetId(taskDatasetId).taskTypeId(taskTypeId).fileTotal((long)annotationFilesId.size()).dataType(dataType).taskRule(taskRule).isFinish(false).taskDesc(taskDesc).taskUser(taskUser).createTime(now).updateTime(now).build();
        task.setCreateMonth(DateUtils.formatAsYearMonthEn(now));
        task.setCreateWeek(DateUtils.formatAsYearWeekEn(now));
        task.setCreateDay(DateUtils.formatAsDateEn(now));
        //后期更新人员和创建人员需要前端传参
        task.setCreateUser("0");
        task.setUpdateUser("0");
        //插入后才能作为其他表需要的外键
        boolean saveTask = save(task);
        //2. 设置表qa_task_entity
        boolean isEntityInsert = false;
        boolean isRelationInsert = false;
        boolean isFileInsert = false;
        for (String entityId:entityList){
            String taskEntityId = idGenerate.generate().toString();
            isEntityInsert= annotationTaskMapper.insertEntityList(taskEntityId,taskId,entityId);
        }
        //3. 设置表qa_file
        for (String fileId:annotationFilesId){
            String taskFileId = idGenerate.generate().toString();
            isRelationInsert= annotationTaskMapper.insertFileList(taskFileId,taskId,fileId);
        }
        //4. 设置表qa_task_relation
        for (String relationId:relationList){
            String taskRelationId = idGenerate.generate().toString();
            isFileInsert= annotationTaskMapper.insertRelationList(taskRelationId,taskId,relationId);
        }
        return saveTask&&isEntityInsert&&isRelationInsert&&isFileInsert;
    }

    /**
     * 查询所有任务
     * @param curPage
     * @param maxPage
     * @param keyword
     * @return
     */

    @Override
    public R<Page<TasksListVO>> findAll(Integer curPage, Integer maxPage, String keyword) {
        //1. todo 需要先从数据库查询出所有的task
        QueryWrapper<AnnotationTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("task_name",keyword);
        Page<AnnotationTask> taskPage = new Page<>(curPage,maxPage);
        Page<AnnotationTask> resultTask = baseMapper.selectPage(taskPage,queryWrapper);
        List<TasksListVO> tasksListVOList = new ArrayList<>();
        //2. todo 对于tasktype是外键id，就需要查询出tasktype的名称,还有所属数据集
        List<AnnotationTask> records = resultTask.getRecords();
        for (AnnotationTask task:records){
            String dataName = annotationTaskMapper.selectDatasetNameByTaskDataSetId(task.getTaskDataSetId());
            String taskTypeName = annotationTaskMapper.selectTypeNameByTaskDataSetId(task.getTaskTypeId());
            //3. todo 查询到信息后将其填充到TasksListVO中
            TasksListVO tasksListVO = new TasksListVO();
            tasksListVO.setId(task.getId());
            tasksListVO.setTaskName(task.getTaskName());
            tasksListVO.setDataSetName(dataName);
            tasksListVO.setTaskType(taskTypeName);
            tasksListVO.setDataType(task.getDataType());
            tasksListVO.setFileNumber(task.getFileTotal());
            tasksListVO.setTaskUser(task.getTaskUser());
            tasksListVO.setCreateTime(task.getCreateTime());
            tasksListVO.setUpdateTime(task.getUpdateTime());
            tasksListVOList.add(tasksListVO);
        }
        //4. todo 构建新的Page<TaskkListVO>对象
        Page<TasksListVO> tasksListVOPage = new Page<>(curPage,maxPage);
        tasksListVOPage.setRecords(tasksListVOList);
        tasksListVOPage.setTotal(resultTask.getTotal());//设置总记录数
        tasksListVOPage.setCurrent(resultTask.getCurrent());//设置当前页码
        tasksListVOPage.setSize(resultTask.getSize());//设置每页记录数
        return R.success(tasksListVOPage);
    }
    /**
     * 通过id删除任务
     */
    @Override
    public R<Boolean> deleteById(String id) {
        //需要先删除中间表的信息：可能存在删除中间表失败，此时也要返回错误信息,中间表为qa_task_entity,qa_task_relation,qa_task_file
        //删除实体表
        //先查询有多少条，如果是0条直接跳过删除
        int entityCounts = annotationTaskMapper.selectTaskEntityCountByTaskId(id);
        if (entityCounts>0){
            Integer isDeleteTaskEntity = annotationTaskMapper.deleteTaskEntityByTaskId(id);
            if (isDeleteTaskEntity!=entityCounts){
                throw BizException.wrap("TaskServiceImpl：删除任务列表时，Entity中间表失败");
            }
        }
        //删除关系表
        int relationCounts = annotationTaskMapper.selectTaskRelationCountByTaskId(id);
        if (relationCounts>0){
            Integer isDeleteTaskRelation = annotationTaskMapper.deleteTaskRelationByTaskId(id);
            if (isDeleteTaskRelation!=relationCounts){
                throw BizException.wrap("TaskServiceImpl：删除任务列表时，Relation中间表失败");
            }
        }
        //删除文件表
        int fileCounts = annotationTaskMapper.selectTaskFilesCountByTaskId(id);
        if (fileCounts>0){
            Integer isDeleteTaskFiles=  annotationTaskMapper.deleteTaskFilesByTaskId(id);
            if (isDeleteTaskFiles!=fileCounts){
                throw BizException.wrap("TaskServiceImpl：删除任务列表时，Files中间表失败");
            }
        }
        //删除中间表后删除自己的信息即可。
        boolean isDelete = removeById(id);
        return R.success(isDelete);
    }
    /**
     * 查询详情
     * @param id
     * @return
     */
    @Override
    public R<TaskInfoVO> findById(String id) {
        //需要查询task表、三张中间表，通过三张中间表关联到对应的名称
        AnnotationTask taskInfo = getById(id);
        List<String> entityNames = annotationTaskMapper.selectTaskEntityNamesByTaskId(id);
        List<String> relationNames = annotationTaskMapper.selectTaskRelationNamesByTaskId(id);
        List<String> fileNames = annotationTaskMapper.selectTaskFileNamesByTaskId(id);
        String taskDataset = annotationTaskMapper.selectDatasetNameByTaskDataSetId(taskInfo.getTaskDataSetId());
        String typeName = annotationTaskMapper.selectTypeNameByTaskDataSetId(taskInfo.getTaskTypeId());
        TaskInfoVO taskInfoVO = TaskInfoVO.builder().id(taskInfo.getId())
                .taskName(taskInfo.getTaskName())
                .taskDesc(taskInfo.getTaskDesc())
                .taskDataset(taskDataset)
                .taskType(typeName)
                .entities(entityNames)
                .relations(relationNames)
                .files(fileNames)
                .fileTotal(taskInfo.getFileTotal())
                .dataType(taskInfo.getDataType())
                .taskRule(taskInfo.getTaskRule())
                .isFinish(taskInfo.isFinish())
                .taskUser(taskInfo.getTaskUser())
                .createMonth(taskInfo.getCreateMonth())
                .createWeek(taskInfo.getCreateWeek())
                .createDay(taskInfo.getCreateDay())
                .createTime(taskInfo.getCreateTime())
                .createUser(taskInfo.getCreateUser())
                .updateTime(taskInfo.getUpdateTime())
                .updateUser(taskInfo.getUpdateUser())
                .build();
        return R.success(taskInfoVO);
    }
}
