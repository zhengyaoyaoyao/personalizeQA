package com.personalize.personalizeqa.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.vo.RelationListVO;

public interface IRelationService extends IService<Relation> {
    boolean insert(String relationName,String description,String annotationName);

    R<Page<Relation>> findAll(Integer curPage, Integer maxPage, String keyword);

    Boolean deleteById(String id);

    Boolean updateById(String id,String description,String relationName,String annotationName);

    RelationListVO relationList();

    boolean isNotExist(String relationName);
}
