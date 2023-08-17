package com.personalize.personalizeqa.strategy;

import com.personalize.personalizeqa.dto.FileDTO;
import com.personalize.personalizeqa.entity.File;
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
    public boolean delete(List<FileDTO> list);
}
