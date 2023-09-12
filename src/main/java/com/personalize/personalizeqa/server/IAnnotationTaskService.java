package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.AnnotationTask;
import com.personalize.personalizeqa.vo.TaskInfoVO;
import com.personalize.personalizeqa.vo.TasksListVO;

import java.util.List;

public interface IAnnotationTaskService extends IService<AnnotationTask> {
    boolean insert(String taskName, String taskDatasetId, List<String> annotationFilesId,String taskTypeId, String dataType, List<String> entityList, List<String> relationList, String taskRule, String taskDesc, String taskUser);

    R<Page<TasksListVO>> findAll(Integer curPage, Integer maxPage, String keyword);

    R<Boolean> deleteById(String id);

    R<TaskInfoVO> findById(String id);
}
