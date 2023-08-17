package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.entity.File;
import com.personalize.personalizeqa.mapper.FileMapper;
import com.personalize.personalizeqa.server.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
    @Override
    public boolean upload(File file) {
        return saveOrUpdate(file);
    }
}
