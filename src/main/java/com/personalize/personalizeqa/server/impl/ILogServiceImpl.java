package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.Log;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.mapper.ILogMapper;
import com.personalize.personalizeqa.server.ILogService;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class ILogServiceImpl extends ServiceImpl<ILogMapper, Log> implements ILogService {
    @Autowired
    private IdGenerate<Long> idGenerate;
//    @Override
//    public void insertLog(String actionName) {
//        String id = idGenerate.generate().toString();
//        LocalDateTime now= LocalDateTime.now();
//        UserDTO user = UserHolder.getUser();
//        Log log = Log.builder().id(id).actionName(actionName).actionUser(user.getUsername()).actionAuthority(user.getAuthority()).actionTime(now).build();
//        save(log);
//    }

    @Override
    public R<Page<Log>> findAll(Integer curPage, Integer maxPage, String keyword) {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("action_user",keyword);
        Page<Log> logPage = new Page<>(curPage,maxPage);
        Page<Log> resultLog = baseMapper.selectPage(logPage,queryWrapper);
        return R.success(resultLog);
    }
}
