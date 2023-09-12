package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.Log;
import com.personalize.personalizeqa.entity.R;

import java.time.LocalDateTime;

public interface ILogService extends IService<Log> {
    public void insertLog(String actionName);

    R<Page<Log>> findAll(Integer page, Integer perPage, String keyword);
}
