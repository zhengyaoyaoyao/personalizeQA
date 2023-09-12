package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.TaskType;
import com.personalize.personalizeqa.mapper.TaskTypeMapper;
import com.personalize.personalizeqa.server.ITaskTypeService;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import com.personalize.personalizeqa.vo.DataListVO;
import com.personalize.personalizeqa.vo.TaskTypeListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TaskTypeServiceImpl extends ServiceImpl<TaskTypeMapper, TaskType> implements ITaskTypeService {
    @Autowired
    private  IdGenerate<Long> idGenerate;
    @Autowired
    private  TaskTypeMapper taskTypeMapper;
    @Override
    public boolean insert(String taskName, String description) {
        String id = idGenerate.generate().toString();
        TaskType taskType = TaskType.builder().id(id).typeName(taskName).typeDesc(description).build();
        boolean save = save(taskType);
        return save;
    }

    /**
     * 根据名称模糊查询
     * @param curPage
     * @param maxPage
     * @param keyword
     * @return
     */
    @Override
    public R<Page<TaskType>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<TaskType> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type_name",keyword);
        Page<TaskType> dataSetPage = new Page<>(curPage,maxPage);
        Page<TaskType> result = baseMapper.selectPage(dataSetPage, queryWrapper);
        return R.success(result);
    }

    /**
     * 根据id删除任务类型
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(String id) {
        boolean isDelete = removeById(id);
        return isDelete;
    }
    /**
     * 更新信息
     */
    @Override
    public Boolean updateById(String id,String description, String taskName) {
        TaskType byId = getById(id);
        byId.setTypeName(taskName);
        byId.setTypeDesc(description);
        boolean isUpdate = updateById(byId);
        return isUpdate;
    }

    @Override
    public TaskTypeListVO taskTypeList() {
        List<Map<String,String>> taskTypeList = taskTypeMapper.selectTaskTypeNames();
        TaskTypeListVO taskTypeListVO = new TaskTypeListVO();
        taskTypeListVO.setTaskTypeList(taskTypeList);
        return taskTypeListVO;
    }
}
