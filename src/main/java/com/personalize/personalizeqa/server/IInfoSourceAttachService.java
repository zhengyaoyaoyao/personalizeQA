package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.InfoSourceAttach;

public interface IInfoSourceAttachService extends IService<InfoSourceAttach> {
    boolean upload(InfoSourceAttach infoSourceAttach);
}
