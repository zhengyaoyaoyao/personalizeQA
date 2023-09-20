package com.personalize.personalizeqa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.ITaskService;
import com.personalize.personalizeqa.vo.TaskShowListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/task")
public class TaskConroller {
    @Autowired
    private ITaskService taskService;
    @OperationLogging(description = "新建采集任务",type = OperationType.INSERT)
    @PostMapping("/insert")
    public R<Boolean> insertTask(@RequestParam("taskName")String taskName,@RequestParam("taskCollectionName")String taskCollectionName, @RequestParam("charge")String charge, @RequestParam("proMembers") List<String> proMembers,
                                 @RequestParam("taskTime")String taskTime,@RequestParam("infoSource")String infoSource,
                                 @RequestParam("infoSourceRule")String infoSourceRule,@RequestParam("taskNote")String taskNote,@RequestParam("status")Boolean status){
        Boolean insert =  taskService.insert(taskName,taskCollectionName,charge,proMembers,taskTime,infoSource,infoSourceRule,taskNote,status);
        return R.success(insert);
    }
    @GetMapping("/findall")
    public R<Page<TaskShowListVO>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        R<Page<TaskShowListVO>> tasks = taskService.findAll(page,perPage,keyword);
        return tasks;
    }
    @OperationLogging(description = "更新采集任务信息",type = OperationType.UPDATE)
    @PostMapping("/update")
    public R<Boolean> updateById(@RequestParam("id")String id,@RequestParam("taskName")String taskName,@RequestParam("taskCollectionName")String taskCollectionName, @RequestParam("charge")String charge, @RequestParam("proMembers") List<String> proMembers,
                                 @RequestParam("taskTime")String taskTime,@RequestParam("taskNote")String taskNote,
                                 @RequestParam("status")Boolean status){
        Boolean isUpdate =  taskService.updateById(id,taskName,taskCollectionName,charge,proMembers,taskTime,taskNote,status);
        return R.success(isUpdate);
    }
    @OperationLogging(description = "删除信源任务",type = OperationType.DELETE)
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        Boolean isDelete = taskService.deleteById(id);
        return R.success(isDelete);
    }
    //上传文件就对文件的内容进行校验，上传的文件需要有集合信息
    @PostMapping("/uploadFiles")
    public R<Boolean> uploadFiles(@RequestParam("id")String id, @RequestParam("taskCollectionName")String taskCode, @RequestParam("files")MultipartFile[] files){
        if (id!=null){
            return taskService.uploadFilesById(id,taskCode,files);
        }else {
            log.error("对某个任务数据集添加文件时，id为空");
            return R.fail("id为空");
        }
    }
    @GetMapping("/isNotExist")
    public R<Boolean> isNotExist(@RequestParam("taskName")String taskName){
        boolean noExist = taskService.isNotExist(taskName);
        return R.success(noExist);
    }
}
