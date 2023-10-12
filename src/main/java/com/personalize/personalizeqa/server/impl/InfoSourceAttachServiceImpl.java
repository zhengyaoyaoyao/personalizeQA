package com.personalize.personalizeqa.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.InfoSourceAttach;
import com.personalize.personalizeqa.mapper.InfoSourceAttachMapper;
import com.personalize.personalizeqa.server.IInfoSourceAttachService;
import com.personalize.personalizeqa.utils.UserHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class InfoSourceAttachServiceImpl  extends ServiceImpl<InfoSourceAttachMapper, InfoSourceAttach> implements IInfoSourceAttachService {
    @Override
    public boolean upload(InfoSourceAttach infoSourceAttach) {
        LocalDateTime now  = LocalDateTime.now();
        infoSourceAttach.setCreateTime(now);
        infoSourceAttach.setUpdateTime(now);
        UserDTO user = UserHolder.getUser();
        infoSourceAttach.setCreateUser(user.getUsername());
        infoSourceAttach.setUpdateUser(user.getUsername());
        return saveOrUpdate(infoSourceAttach);
    }
}
