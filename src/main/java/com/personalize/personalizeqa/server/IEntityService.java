package com.personalize.personalizeqa.server;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personalize.personalizeqa.entity.Entity;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.Relation;
import com.personalize.personalizeqa.vo.EntityListVO;

public interface IEntityService extends IService<Entity> {
    boolean insert(String entityName,String description,String annotationName);

    R<Page<Entity>> findAll(Integer curPage, Integer maxPage, String keyword);

    Boolean deleteById(String id);

    Boolean updateById(String id,String description,String entityName,String annotationName);

    EntityListVO entityList();

    boolean isNotExist(String entityName);
}
