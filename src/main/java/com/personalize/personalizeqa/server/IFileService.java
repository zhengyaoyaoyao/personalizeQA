package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.dto.FileListDTO;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.vo.TaskNewFilesListVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IFileService extends IService<File> {
    boolean upload(File file);

    Integer deleteFilesByDataId(String dataId);
    boolean deleteFilesById(String id);

    R<FileListDTO> findAll();

    R<File> findById(String id);

    R<File> updateInfo(String id, String fileName);

    void download(HttpServletRequest request, HttpServletResponse response, String id)throws Exception;

    void downloadDataSet(HttpServletRequest request, HttpServletResponse response, String dataName, String id) throws Exception;

    TaskNewFilesListVO getFilesNameListByDataName(String dataName);
}
