package com.personalize.personalizeqa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.IAnnotationTaskService;
import com.personalize.personalizeqa.vo.TaskInfoVO;
import com.personalize.personalizeqa.vo.TasksListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/annotationtask")
@CrossOrigin // 允许来自指定源的请求
@RestController
@Slf4j
public class AnnotationTaskController {
    @Autowired
    private IAnnotationTaskService annotationTaskService;

    /**
     * 插入一个标注任务
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
    @OperationLogging(description = "新建标注任务",type = OperationType.INSERT)
    @PostMapping("/insert")
    public R<Boolean> insert(@RequestParam("taskName")String taskName, @RequestParam("taskDatasetId")String taskDatasetId, @RequestParam("annotationFilesId")List<String> annotationFilesId,
                             @RequestParam("taskTypeId")String taskTypeId,@RequestParam("dataType")String dataType,@RequestParam(value = "entityList",required = false)List<String> entityList,
                             @RequestParam(value = "relationList",required = false)List<String> relationList,@RequestParam("taskRule")String taskRule,@RequestParam("taskDesc")String taskDesc,
                             @RequestParam("taskUser")String taskUser){
        boolean insert = annotationTaskService.insert(taskName, taskDatasetId, annotationFilesId, taskTypeId, dataType, entityList, relationList, taskRule, taskDesc, taskUser);
        if (insert){
            return R.success(insert);
        }else {
            return R.fail("新建任务失败,请重新再试");
        }
    }

    /**
     * 查询信息
     * @param page
     * @param perPage
     * @param keyword
     * @return
     */
    @GetMapping("/findall")
    public R<Page<TasksListVO>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        log.info("关键词是什么",keyword);
        R<Page<TasksListVO>> taskList = annotationTaskService.findAll(page,perPage,keyword);
        return taskList;
    }

    /**
     * 删除标注任务
     * @param id
     * @return
     */
    @OperationLogging(description = "根据id删除标注任务",type = OperationType.DELETE)
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id) {
        R<Boolean> isDelete = annotationTaskService.deleteById(id);
        return isDelete;
    }
    @GetMapping("/info")
    public R<TaskInfoVO>  findById(@RequestParam("id")String id){
        R<TaskInfoVO> taskInfoVOR = annotationTaskService.findById(id);
        return taskInfoVOR;
    }
}
