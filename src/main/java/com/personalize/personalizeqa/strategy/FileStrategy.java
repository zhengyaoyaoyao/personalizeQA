package com.personalize.personalizeqa.strategy;

import com.personalize.personalizeqa.dto.FileDeleteDO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStrategy {
    /**
     * 文件上传
     * @param file
     * @return
     */
    public File upload(MultipartFile file,String folder);
    /**
     * file delete
     */
    public boolean delete(List<FileDeleteDO> list);
    public InfoSourceFile uploadInfoSourceFile(MultipartFile file,String folder);

    void deleteFolder(String rel_folder);
}
