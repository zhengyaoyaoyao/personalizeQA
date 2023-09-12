package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.server.IInfoSourceFileService;
import com.personalize.personalizeqa.vo.InfoSourceFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin // 允许来自指定源的请求
@RequestMapping("/infosourcefile")
public class InfoSourceFileController {
    @Autowired
    private IInfoSourceFileService infoSourceFileService;
    @GetMapping("/findAll")
    public R<List<InfoSourceFileVO>> findAll(@RequestParam("id")String taskId){
        List<InfoSourceFileVO> result =  infoSourceFileService.findAll(taskId);
        return R.success(result);
    }
    @GetMapping("/getContent")
    public R<String> getContent(@RequestParam("id")String id){
        String fileContent = infoSourceFileService.getFileContent(id);
        return R.success(fileContent);
    }
    @GetMapping("/deleteFile")
    public R<Boolean> deleteFile(@RequestParam("id")String id){
        Boolean isDelete = infoSourceFileService.deleteFile(id);
        return R.success(isDelete,"删除成功");
    }
    @GetMapping("/checkFile")
    public R<Boolean> checkFile(@RequestParam("id")String id){
        R<Boolean> result = infoSourceFileService.checkFile(id);
        return result;
    }
}
