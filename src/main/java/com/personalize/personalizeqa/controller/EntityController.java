package com.personalize.personalizeqa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.TaskType;
import com.personalize.personalizeqa.server.IEntityService;
import com.personalize.personalizeqa.server.ITaskTypeService;
import com.personalize.personalizeqa.vo.EntityListVO;
import com.personalize.personalizeqa.vo.TaskTypeListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/entity")
public class EntityController {
    @Autowired
    private IEntityService entityService;
    @PostMapping("/insert")
    public R<Boolean> insertTaskType(@RequestParam("description") String description, @RequestParam("entityname")String taskName){
        log.info("taskName:{},taskDescription:{}",taskName,description);
        boolean insert = entityService.insert(taskName, description);
        return R.success(insert);
    }

    /**
     * 根据名称模糊查询
     * @param page
     * @param perPage
     * @param keyword
     * @return
     */
    @GetMapping("/findall")
    public R<Page<Entity>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        log.info("关键词是什么",keyword);
        R<Page<Entity>> taskTypeList = entityService.findAll(page,perPage,keyword);
        return taskTypeList;
    }

    /**
     * 根据id删除任务类型
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        Boolean isDelete  = entityService.deleteById(id);
        return R.success(isDelete);
    }

    /**
     * 更新操作
     * @param id
     * @param description
     * @param taskName
     * @return
     */
    @PostMapping("/update")
    public R<Boolean> updateById(@RequestParam("id")String id,@RequestParam("description") String description,@RequestParam("taskname")String taskName){
        Boolean isUpdate =entityService.updateById(id,description,taskName);
        return R.success(isUpdate);
    }
    /**
     * 查询所有任务类型
     */
    @GetMapping(value = "/tasktypeList")
    public R<EntityListVO> taskTypeList(){
        EntityListVO taskTypeListVO =  entityService.taskTypeList();
        return R.success((taskTypeListVO));
    }
}
