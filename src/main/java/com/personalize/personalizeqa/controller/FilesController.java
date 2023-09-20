package com.personalize.personalizeqa.controller;


import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.dto.FileListDTO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.enumeration.OperationType;
import com.personalize.personalizeqa.server.IFileService;
import com.personalize.personalizeqa.vo.TaskNewFilesListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/files")
@CrossOrigin // 允许来自指定源的请求
@Slf4j
@RestController
public class FilesController {
    @Autowired
    private IFileService fileService;
    @GetMapping("/findall")
    public R<FileListDTO> findAll(){
        R<FileListDTO> fileList = fileService.findAll();
        return fileList;
    }
    @GetMapping("/info")
    public R<File> findById(@RequestParam("id")String id){
        log.info("id",id);
        R<File> file = fileService.findById(id);
        return file;
    }
    @OperationLogging(description = "更新文件信息",type = OperationType.UPDATE)
    @GetMapping("/update")
    public R<File> updateById(@RequestParam("id")String id,@RequestParam("submittedFileName")String fileName){
        R<File> file = fileService.updateInfo(id,fileName);
        return file;
    }
//    @OperationLogging(description = "下载文件",type = OperationType.EXPORT)
    @GetMapping(value = "/download" ,produces = "application/octet-stream")
    public void download(@RequestParam(value = "id") String id , HttpServletRequest request, HttpServletResponse response) throws Exception{
        fileService.download(request,response,id);
    }
    @OperationLogging(description = "删除文件",type = OperationType.DELETE)
    @GetMapping("/delete")
    public R<Boolean> deleteById(@RequestParam("id")String id){
        boolean isDelete = fileService.deleteFilesById(id);
        return R.success(isDelete);
    }
    @GetMapping("/getFilesNameListByDataName")
    public R<TaskNewFilesListVO> getFilesNameListByDataName(@RequestParam(value = "selectDataName")String dataName){
        TaskNewFilesListVO taskNewFilesListVO = fileService.getFilesNameListByDataName(dataName);
        return R.success(taskNewFilesListVO);
    }
}
