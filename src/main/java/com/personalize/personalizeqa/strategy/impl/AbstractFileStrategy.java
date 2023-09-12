package com.personalize.personalizeqa.strategy.impl;


import cn.hutool.core.io.file.FileNameUtil;
import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.exception.BizException;
import com.personalize.personalizeqa.exception.code.ExceptionCode;
import com.personalize.personalizeqa.properties.FileServerProperties;
import com.personalize.personalizeqa.strategy.FileStrategy;
import com.personalize.personalizeqa.utils.FileDataTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件处理的抽象类，每个文件系统都要实现这些功能，屏蔽底层具体的文件系统。
 */
@Slf4j
public abstract class AbstractFileStrategy implements FileStrategy {
    private static final String FILE_SPLIT = ".";
    @Autowired
    protected FileServerProperties fileProperties;

    protected FileServerProperties.Properties properties;

    //标注：上传文件
    @Override
    public File upload(MultipartFile multipartFile,String folder) {
        try {
            if (!multipartFile.getOriginalFilename().contains(FILE_SPLIT)){
                log.debug("缺少后缀名");
            }
            File file = File.builder().dataType(FileDataTypeUtil.getDataType(multipartFile.getContentType()))
                    .submittedFileName(multipartFile.getOriginalFilename())
                    .isDelete(false)//文件是否被删除
                    .folder(folder) // 所属的文件夹
                    .size(multipartFile.getSize()) //文件大小
                    .contextType(multipartFile.getContentType())
                    .ext(FileNameUtil.extName(multipartFile.getOriginalFilename())).build();
            uploadFile(file,multipartFile);
            return file;
        }catch (Exception e){
            log.error("e={}",e);
            throw BizException.wrap(ExceptionCode.BASE_VALID_PARAM.build("文件上传失败"));
        }
    }
    public abstract void uploadFile(File file,MultipartFile multipartFile) throws Exception;

    @Override
    public InfoSourceFile uploadInfoSourceFile(MultipartFile multipartFile, String taskCode) {
        try {
            if (!multipartFile.getOriginalFilename().contains(FILE_SPLIT)){
                log.debug("缺少后缀名");
            }
            InfoSourceFile infoSourceFile = InfoSourceFile.builder().dataType(FileDataTypeUtil.getDataType(multipartFile.getContentType()))
                    .submittedFileName(multipartFile.getOriginalFilename())
                    .isDelete(false)
                    .taskCode(taskCode).size(multipartFile.getSize()).contextType(multipartFile.getContentType())
                    .ext(FileNameUtil.extName(multipartFile.getOriginalFilename())).build();
            uoloadInfoSourceFileImpl(infoSourceFile,multipartFile);
            return infoSourceFile;
        }catch (Exception e){
            log.error("上传文件失败,{}",e);
            throw BizException.wrap(ExceptionCode.BASE_VALID_PARAM.build("文件上传失败"));
        }
    }
    public abstract void uoloadInfoSourceFileImpl(InfoSourceFile infoSourceFile,MultipartFile multipartFile) throws Exception;

    @Override
    public boolean delete(List<FileDeleteDO> list) {
        if (list==null||list.isEmpty()){
            return true;
        }
        boolean flag = false;//删除操作是否成功的表示
        for (FileDeleteDO fileDeleteDO : list){
            try {
                delete(fileDeleteDO);
                flag = true;
            }catch (Exception e){
                log.error("e={}",e);
            }
        }
        return flag;
    }
    public abstract void delete(FileDeleteDO fileDTO);

    @Override
    public abstract void deleteFolder(String rel_folder);

    protected String getUriPrefix(){
        if (StringUtils.isNotEmpty(properties.getUriPrefix())){
            return properties.getUriPrefix();
        }else {
            return properties.getEndpoint();
        }
    }
}
