package com.personalize.personalizeqa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.entity.R;
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

    //todo 1.新建一个任务
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
    //todo 2.查询所有信息，且模糊查询
    @GetMapping("/findall")
    public R<Page<TasksListVO>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        log.info("关键词是什么",keyword);
        R<Page<TasksListVO>> taskList = annotationTaskService.findAll(page,perPage,keyword);
        return taskList;
    }

    //todo 3.删除
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        R<Boolean> isDelete = annotationTaskService.deleteById(id);
        return isDelete;
    }
    //todo 4. 更新任务信息

    //todo 5. 查看详情，通过id查询信息
    @GetMapping("/info")
    public R<TaskInfoVO>  findById(@RequestParam("id")String id){
        R<TaskInfoVO> taskInfoVOR = annotationTaskService.findById(id);
        return taskInfoVOR;
    }
}
