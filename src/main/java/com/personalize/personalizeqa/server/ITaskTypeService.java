package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.TaskType;
import com.personalize.personalizeqa.vo.TaskTypeListVO;

public interface ITaskTypeService extends IService<TaskType> {
    boolean insert(String taskName,String description);

    R<Page<TaskType>> findAll(Integer curPage, Integer maxPage, String keyword);

    Boolean deleteById(String id);

    Boolean updateById(String id,String description,String taskName);

    TaskTypeListVO taskTypeList();
}
