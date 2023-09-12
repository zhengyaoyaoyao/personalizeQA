package com.personalize.personalizeqa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.server.IRelationService;
import com.personalize.personalizeqa.vo.RelationListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/relation")
public class RelationController {
    @Autowired
    private IRelationService relationService;
    @PostMapping("/insert")
    public R<Boolean> insertTaskType(@RequestParam("description") String description, @RequestParam("relationName")String relationName,@RequestParam("annotationName")String annotationName){
        log.info("relationName:{},relationDescription:{}",relationName,description);
        boolean insert = relationService.insert(relationName, description,annotationName);
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
    public R<Page<Relation>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        log.info("关键词是什么",keyword);
        R<Page<Relation>> taskTypeList = relationService.findAll(page,perPage,keyword);
        return taskTypeList;
    }

    /**
     * 根据id删除任务类型
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        Boolean isDelete  = relationService.deleteById(id);
        return R.success(isDelete);
    }

    /**
     * 更新操作
     * @param id
     * @param description
     * @param relationName
     * @return
     */
    @PostMapping("/update")
    public R<Boolean> updateById(@RequestParam("id")String id,@RequestParam("description") String description,@RequestParam("relationName")String relationName,@RequestParam("annotationName")String annotationName){
        Boolean isUpdate =relationService.updateById(id,description,relationName,annotationName);
        return R.success(isUpdate);
    }
    /**
     * 查询所有任务类型
     */
    @GetMapping(value = "/relationList")
    public R<RelationListVO> relationList(){
        RelationListVO relationListVO =  relationService.relationList();
        return R.success((relationListVO));
    }
    /**
     * 确保唯一实体
     * @param relationName
     * @return
     */
    @GetMapping("/isNotExist")
    public R<Boolean> isNotExist(@RequestParam("relationName")String relationName){
        boolean noExist = relationService.isNotExist(relationName);
        return R.success(noExist);
    }
}
