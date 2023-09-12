package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.InfoSource;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.vo.InfoSourceListVO;
import com.personalize.personalizeqa.vo.TaskGetInfoSourcesVO;

import java.util.List;

public interface IInfoSourceService extends IService<InfoSource> {



    R<Page<InfoSource>> findAll(Integer page, Integer perPage, String keyword);

    Boolean deleteById(String id);

    Boolean updateById(String id, String infoSourceName, String infoSourceUrl, String infoSourceRule, String infoSourceDesc);

    InfoSourceListVO infoSourceList();

    boolean isNotExist(String infoSourceName);

    boolean insert(String infoSourceName, String infoSourceUrl, String infoSourceRule, String infoSourceDesc);

    List<TaskGetInfoSourcesVO> getInfoSources();

    InfoSource getByName(String infoSourceName);
}
