package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.entity.InfoSourceFile;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.vo.InfoSourceFileVO;

import java.util.List;

public interface IInfoSourceFileService  extends IService<InfoSourceFile> {
    boolean upload(InfoSourceFile infoSourceFile);

    List<InfoSourceFileVO> findAll(String id);
    String getFileContent(String id);

    Boolean deleteFile(String id);

    R<Boolean> checkFile(String id);

    String getClientFileContent(String id);
}
