package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.mapper.FileMapper;

public interface IFileService extends IService<File> {
    boolean upload(File file);
}
