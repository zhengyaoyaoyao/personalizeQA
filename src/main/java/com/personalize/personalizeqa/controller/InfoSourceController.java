package com.personalize.personalizeqa.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.IInfoSourceService;
import com.personalize.personalizeqa.server.IRelationService;
import com.personalize.personalizeqa.vo.InfoSourceListVO;
import com.personalize.personalizeqa.vo.RelationListVO;
import com.personalize.personalizeqa.vo.TaskGetInfoSourcesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/infosource")
public class InfoSourceController {
    @Autowired
    private IInfoSourceService infoSourceService;
//    @OperationLogging(description = "新建信源",type = OperationType.INSERT)
    @PostMapping("/info-source")
    public R<Boolean> insertTaskType(@RequestParam("infoSourceName") String infoSourceName,@RequestParam("infoSourceUrl")String infoSourceUrl,@RequestParam("infoSourceApi")String infoSourceApi,@RequestParam("infoSourceRule")String infoSourceRule,@RequestParam("infoSourceDesc")String infoSourceDesc){
        boolean insert = infoSourceService.insert(infoSourceName,infoSourceUrl,infoSourceApi,infoSourceRule,infoSourceDesc);
        return R.success(insert);
    }

    /**
     * 根据名称模糊查询
     * @param page
     * @param perPage
     * @param keyword
     * @return
     */
    @GetMapping("/info-sources")
    public R<Page<InfoSource>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage, @RequestParam(value = "keyword",required = false)String keyword){
        R<Page<InfoSource>> infoSources = infoSourceService.findAll(page,perPage,keyword);
        return infoSources;
    }

    /**
     * 根据id删除任务类型
     * @param id
     * @return
     */
    @OperationLogging(description = "删除信源信息",type = OperationType.DELETE)
    @DeleteMapping("/info-sources/{id}")
    public R<Boolean> deleteById(@PathVariable("id")String id){
        Boolean isDelete  = infoSourceService.deleteById(id);
        return R.success(isDelete);
    }
    @GetMapping("/info-sources/{id}")
    public R<InfoSource> getById(@PathVariable("id")String id){
        InfoSource infoSource  = infoSourceService.getById(id);
        return R.success(infoSource);
    }
    /**
     * 更新操作
     * @param id
     * @return
     */
//    @OperationLogging(description = "更新信源信息",type = OperationType.UPDATE)
    @PostMapping("/update")
    public R<Boolean> updateById(@RequestParam("id")String id,@RequestParam("infoSourceName") String infoSourceName,@RequestParam("infoSourceUrl")String infoSourceUrl,@RequestParam("infoSourceApi")String infoSourceApi,@RequestParam("infoSourceRule")String infoSourceRule,@RequestParam("infoSourceDesc")String infoSourceDesc){
        Boolean isUpdate =infoSourceService.updateById(id,infoSourceName,infoSourceUrl,infoSourceApi,infoSourceRule,infoSourceDesc);
        return R.success(isUpdate);
    }
    /**
     * 查询所有任务类型
     */
    @GetMapping(value = "/relationList")
    public R<InfoSourceListVO> relationList(){
        InfoSourceListVO infoSourceListVO =  infoSourceService.infoSourceList();
        return R.success((infoSourceListVO));
    }
    /**
     * 确保唯一实体
     * @param infoSourceName
     * @return
     */
    @GetMapping("/isNotExist")
    public R<Boolean> isNotExist(@RequestParam("infoSourceName")String infoSourceName){
        boolean noExist = infoSourceService.isNotExist(infoSourceName);
        return R.success(noExist);
    }
    /**
     * 获得所有id，信源名称，信源规则
     */
    @GetMapping("/getInfoSources")
    public R<List<TaskGetInfoSourcesVO>> getInfoSources(){
        List<TaskGetInfoSourcesVO>  taskGetInfoSourcesVOS = infoSourceService.getInfoSources();
        return R.success(taskGetInfoSourcesVOS);
    }
    @GetMapping("/getByName")
    public R<InfoSource> getByName(@RequestParam("infoSourceName")String infoSourceName){
        InfoSource infoSource = infoSourceService.getByName(infoSourceName);
        return R.success(infoSource);
    }
}
