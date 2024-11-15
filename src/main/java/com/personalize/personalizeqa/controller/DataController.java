package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.IDataSetService;
import com.personalize.personalizeqa.vo.DataListVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/dataset")
@Slf4j
@Api(value = "数据集上传",tags = "数据集上传")
public class DataController {
    @Autowired
    private IDataSetService dataSetService;

    /**
     * 模糊查找查看所有信息
     * @param page 当前页信息
     * @param perPage  每页多少条
     * @param keyword  模糊搜索的关键词
     * @return
     */
    @ApiOperation("查询所有数据集")
    @GetMapping("/findall")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "per_page", value = "每页多少条", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "模糊搜索的关键词", dataType = "String", paramType = "query")
    })
    public R<Page<DataSet>> findAll(@RequestParam(value = "page",defaultValue = "1")Integer page, @RequestParam("per_page")Integer perPage,@RequestParam(value = "keyword",required = false)String keyword){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        log.info("关键词是什么",keyword);
        R<Page<DataSet>> datasetList = dataSetService.findAll(page,perPage,keyword);
        return datasetList;
    }
    @GetMapping("/info")
    public R<DataSet> findById(@RequestParam("id") String id){
        R<DataSet> dataSet = dataSetService.findById(id);
        return dataSet;
    }

    /**
     * 上传文件
     * @param dataName
     * @param dataSetUrl
     * @param dataSetInfo
     * @param files
     * @return
     */
    @OperationLogging(description = "上传数据集",type = OperationType.IMPORT)
    @PostMapping("/upload")
    public R<DataSet> uploader(@RequestParam("dataName") String dataName,
                               @RequestParam("dataSetUrl") String dataSetUrl,
                               @RequestParam("dataSetInfo") String dataSetInfo,
                               @RequestParam("files") MultipartFile[] files){
        //将信息保存到数据库中，dataName作为区分的文件夹，
        return dataSetService.upload(dataName,dataSetUrl,dataSetInfo,files);
    }

    /**
     * 根据id删除数据集
     * @param id
     * @return
     */
    @OperationLogging(description = "根据id删除数据集",type = OperationType.DELETE)
    @GetMapping("/deleteById")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        R<Boolean> isDelete = dataSetService.deleteById(id);
        return isDelete;
    }

    @OperationLogging(description = "更新数据集信息",type = OperationType.UPDATE)
    @GetMapping("/update")
    public R<DataSet> update(@RequestParam("id")String id,
                             @RequestParam("dataName")String dataName,
                             @RequestParam("dataUrl")String dataUrl,
                             @RequestParam("dataInfo")String dataInfo){
        log.info("id:",id);
        log.info("dataNam:",dataName);
        log.info("dataUrl:",dataUrl);
        log.info("dataInfo:",dataInfo);
        R<DataSet> updateDateset = dataSetService.updateById(id, dataName, dataUrl, dataInfo);
        return updateDateset;
    }
//    @OperationLogging(description = "根据数据集上传文件",type = OperationType.IMPORT)
    @PostMapping("/uploadFiles")
    public R<Boolean> uploadFiles(@RequestParam("id")String id,@RequestParam("files") MultipartFile[] files){
        if (id!=null){
            return dataSetService.uploadFilesById(id,files);
        }else {
            log.error("对某个数据集添加文件时，id为空");
            return R.fail("id为空");
        }
    }
    /**
     * 判断是否存在数据集
     */
    @GetMapping("/isNotExist")
    public R<Boolean> isNotExist(@RequestParam("dataName")String dataName){
        boolean noExist = dataSetService.isNotExist(dataName);
        return R.success(noExist);
    }
    @OperationLogging(description = "下载数据集文件",type = OperationType.EXPORT)
    @GetMapping(value = "/download",produces = "application/octet-stream")
    public void download(@RequestParam(value = "id")String id,@RequestParam(value = "dataName")String dataName, HttpServletRequest request, HttpServletResponse response) throws Exception{
        dataSetService.download(request,response,dataName,id);
    }
    @GetMapping(value = "/datasetList")
    public R<DataListVO> datasetList(){
        DataListVO dataListVO = dataSetService.datasetList();
        return R.success(dataListVO);
    }
}
