package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.dto.DataSetListDTO;
import com.personalize.personalizeqa.entity.DataSet;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.IDataSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataset")
@Slf4j
public class DataController {
    @Autowired
    private IDataSetService dataSetService;
    //todo: 1.所有数据展示
    @GetMapping("/findall")
    public R<DataSetListDTO> findAll(@RequestParam("page")String page, @RequestParam("per_page")String perPage){
        log.info("一页多少个：",page);
        log.info("当前页:",perPage);
        R<DataSetListDTO> datasetList = dataSetService.findAll();
        return datasetList;
    }
    //todo: 2.单独数据详情
    @GetMapping("dataset/info/{id}")
    public R<DataSet> findById(@PathVariable("id") String id){
        R<DataSet> dataSet = dataSetService.findById(id);
        return dataSet;
    }

    //todo：3.文件上传
    @PostMapping("/upload")
    public R<DataSet> uploader(@RequestParam("dataName") String dataName,
                               @RequestParam("dataSetUrl") String dataSetUrl,
                               @RequestParam("dataSetInfo") String dataSetInfo,
                               @RequestParam("files") MultipartFile[] files){
        //将信息保存到数据库中，dataName作为区分的文件夹，
        return dataSetService.upload(dataName,dataSetUrl,dataSetInfo,files);
    }
    //todo：4.文件删除

    //todo：5.文件更新

    //todo: 6.文件查找
}
